package de.pp.rm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.pp.rm.data.Drive;
import de.pp.rm.data.RAM;

/**
 * Diese Klasse ist die wo alle Systeminfos in den jeweiligen Methoden
 * ausgelesen werden. Gespeichert werden diese dann in den spezifischen Klassen.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class Windows {
	/**
	 * Wandelt Strings in Zahlen.
	 */
	private NumberFormat nf;

	public Windows() {
		nf = NumberFormat.getInstance(Locale.getDefault());
	}

	/**
	 * Entfernt alle Zeichen die keine Zahlen, Punkte und Kommata sind aus der
	 * übergebenen Variabel.
	 * 
	 * @param text Der zu bearbeitende Text.
	 * @return Einen String der nur aus Zahlen Kommata und Punkten besteht.
	 */
	private String getNumbersFromText(String text) {
		int digit_counter = 0;
		String festplattenSpeicher = "";
		while (digit_counter < text.length()) {
			char test = text.charAt(digit_counter);
			if (Character.isDigit(test) || test == '.' || test == ',') {
				festplattenSpeicher = festplattenSpeicher + text.charAt(digit_counter);

			}
			digit_counter++;
		}
		return festplattenSpeicher;
	}

	/**
	 * Wird dazu genutzt um einen Befehl auf der CMD auszuführen.
	 * 
	 * @param befehl Der auszuführende CMD Befehl.
	 * @return Einen BufferedReader mit der Ausgabe des ausgeführten Befehls.
	 */
	private BufferedReader execute(String befehl) {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", befehl);
		builder.redirectErrorStream(true);
		try {
			Process p = builder.start();
			return new BufferedReader(new InputStreamReader(p.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
/**
 * 
 * @param reader Ein BufferedReader mit dem Befehl.
 * @param zeilenZahl Die Zeilenzahl der Zeile welche man haben will.
 * @return Die Zeile an der angegeben Stelle.
 */
	private String lesen(BufferedReader reader, int zeilenZahl) {
		String line;
		int line_counter = 0;
		while (true) {
			try {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				if (line_counter == zeilenZahl) {
					return line;
				}
				line_counter++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * Gibt alle(ohne Netzwerklaufwerke) Festplatten im System mit allen wichtigen
	 * Infos zurück.
	 * 
	 * @return Eine Liste mit allen Festplatten im System(ohne
	 *         Netzwerklaufwerke).
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Drive> Festplatten() throws IOException, ParseException {
		List<Drive> drives = new ArrayList<Drive>();
		FileSystem fileSystem = FileSystems.getDefault();
		try {
			for (FileStore store : fileSystem.getFileStores()) {
				Drive drive = new Drive();
				drive.setDriveLetter(store.toString().split(" ")[1].charAt(1));
				drive.setDriveName(store.toString().split(" ")[0]);
				drive.setDriveType(store.type());
				drive.setMaxSpace(store.getTotalSpace() / (Math.pow(1024, 3)));
				drive.getDriveUsage().setUsage(100 - (((store.getUsableSpace() / Math.pow(1024d, 3))
						/ (store.getTotalSpace() / Math.pow(1024d, 3))) * 100));
				drives.add(drive);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return drives;

	}

	/**
	 * Führt den passende CMD Befehl auf der CMD aus und entfernt unnötige Angaben.
	 * 
	 * @return Die Anzahl der CPU Kerne als Zahl.
	 */
	public int cpuKerne() {
		BufferedReader r = execute("echo %NUMBER_OF_PROCESSORS%");
		String line = lesen(r, 0);
		String anzahl = line.trim();
		return Integer.parseInt(anzahl);
	}

	/**
	 * Führt den passende CMD Befehl auf der CMD aus und entfernt unnötige Angaben.
	 * 
	 * @return Die aktuelle Auslastung des Prozessors in Prozent.
	 * @throws IOException
	 * @throws ParseException
	 */
	public double cpuAuslastung() throws IOException, ParseException {
		BufferedReader r = execute("wmic cpu get loadpercentage ");
		String line = lesen(r, 2);
		String prozent = line.trim();
		return nf.parse(prozent).doubleValue();
	}

	/**
	 * Führt einen Befehl in der CMD aus und speichert die Ergebnisse im RAM-Objekt.
	 * 
	 * @param ram Das RAM-Objekt wo alles gespeichert werden soll.
	 * @return Das selbe Objekt mit den gespeicherten Daten.
	 * @throws IOException
	 * @throws ParseException
	 */
	public RAM getRamInfo(RAM ram) throws IOException, ParseException {
		String[] ausgabe = new String[128];
		BufferedReader r = execute("systeminfo");
		String line;
		int counter = 0;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			ausgabe[counter] = line;
			counter++;
		}
		ram.getRamUsage().setUsage((freierArbeitsspeicher(ausgabe[25]) / gesamterArbeitsspeicher(ausgabe[24])) * 100);
		ram.setMaxRam(gesamterArbeitsspeicher(ausgabe[24]));
		return ram;
	}

	/**
	 * Gibt den Namen des PC's zurück.
	 * 
	 * @return Den Namen als String
	 */
	public String getPCName() {
		RuntimeMXBean rmx = ManagementFactory.getRuntimeMXBean();
		return rmx.getName().split("@")[1];
	}

	/**
	 * Führt den passende CMD Befehl auf der CMD aus und entfernt unnötige Angaben.
	 * 
	 * @return Die CPU-Frequenz.
	 * @throws IOException
	 * @throws ParseException
	 */
	public double cpuFrequenz() throws IOException, ParseException {
		BufferedReader r = execute("systeminfo");
		String cpuFrequenz = getNumbersFromText(lesen(r, 16).split("~")[1]);
		return nf.parse(cpuFrequenz).doubleValue();
	}

	/**
	 * Entfernt unnötige Informationen.
	 * 
	 * @param line Der String mit den Informationen.
	 * @return Den freien Arbeitsspeicher.
	 * @throws IOException
	 * @throws ParseException
	 */
	public double freierArbeitsspeicher(String line) throws IOException, ParseException {
		line = getNumbersFromText(line);
		return nf.parse(line).doubleValue();
	}

	/**
	 * Entfernt unnötige Informationen.
	 * 
	 * @param line Der String mit den Informationen.
	 * @return Den gesamten Arbeitsspeicher.
	 * @throws IOException
	 * @throws ParseException
	 */
	public double gesamterArbeitsspeicher(String line) throws IOException, ParseException {
		line = getNumbersFromText(line);
		return nf.parse(line).doubleValue();
	}

}
