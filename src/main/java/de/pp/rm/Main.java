package de.pp.rm;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import de.pp.rm.data.CPU;
import de.pp.rm.data.Drive;
import de.pp.rm.data.PC;
import de.pp.rm.data.RAM;
import de.pp.rm.database.CPURepository;
import de.pp.rm.database.DriveRepository;
import de.pp.rm.database.PCRepository;
import de.pp.rm.database.RAMRepository;

/**
 * Dieses Programm ist dazu da die Systeminformationen eines
 * Windows-Rechners(Windows 10) auszulesen und die Ergebnisse in eine Datenbank
 * (in diesem Fall MySQL) hochzuladen. Ein Teil des Programms wird jede Minute
 * ausgeführt und ein anderer Teil einmal am Tag. Zuerst liest das Programm die
 * benötigten Systeminformationen mit Hilfe der Kommandozeile aus und speichert
 * die Ergebnisse in der Datenbank. Mit Systeminformationen sind folgende Werte
 * gemeint: Anzahl der Prozessor-Kerne, die Frequenz des Prozessors, die
 * Aktuelle Auslastung des Prozessors (in Prozent), alle Festplatten im Rechner
 * und deren verfügbarer Speicherplatz, der gesamte Arbeitsspeicher, der aktuell
 * verfügbare Arbeitsspeicher und der Name des Rechners. Jede Minute wird
 * folgendes ausgelesen: Die Aktuelle Auslastung des Prozessors sowie der
 * gesamte Arbeitsspeicher und der freie Arbeitsspeicher. Einmal pro Tag wird
 * folgendes ausgelesen: Anzahl der Prozessor-Kerne, die Frequenz des
 * Prozessors, alle Festplatten im Rechner und deren verfügbarer Speicherplatz
 * und der Name des Rechners. Dann loggt sich das Programm in die Datenbank ein,
 * speichert die Ergebnisse in der Datenbank und loggt sich anschließend wieder
 * aus.
 * 
 * Infos zur RM-Datenbank liegen im Confluence.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 */
public class Main {

	/**
	 * Das Hauptobjekt in dem alles gespeichert wird.
	 */
	private PC pc;

	/**
	 * Das Objekt in dem alle Methoden stehen zum auslesen. @see class.Windows
	 */
	private Windows windows;

	/**
	 * Der String um das Betrienssystem herauszufinden.
	 */
	public static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Die Main Methode des Programms.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Main().run();
	}

	/**
	 * Diese Methode ist die RUN-Methode des Programms, diese steuert den Ablauf.
	 * 
	 * <p>
	 * Zum Ablauf: am Anfang wird überprüft ob sich der PC schon in der Datenbank
	 * befindet. Wenn nicht wird ein neuer PC erstellt und hochgeladen falls doch
	 * wird er aus der Datenbank geladen und gespeichert.
	 * 
	 * Dann geht es in die endlose while Schleife: Dort wird als erstes die RAM Info
	 * ausgelesen. Dann geht es wenn der counter auf 0 steht in die IF-Abzweigung:
	 * Dort werden dann die Systeminformationen, die nur einmal am Tag geupdatet
	 * werden sollen, abgefragt, gelesen und hochgeladen. Danach werden die
	 * Informationen ausgelesen die einmal die Minute abgefragt werden sollen. Am
	 * Schluss wird das Programm 60 sekunden pausiert und der Counter um 1
	 * hochgezählt.
	 * </p>
	 */
	public void run() {
		windows = new Windows();
		System.out.println("Start");
		pc = PCRepository.get(windows.getPCName());
		if (pc == null) {// hier wird überprüft ob sich der pc schon in der Datenbank befindet
			pc = new PC();
			pc.setName(windows.getPCName());
			PCRepository.insert(pc);

			pc.setName(windows.getPCName());
			pc.setDrives(new LinkedList<Drive>()); // wenn nicht wird hier ein neuer erstellt
			pc.setCpu(new CPU());
			pc.setRam(new RAM());
			try {
				pc.setDrives(windows.Festplatten());
				pc.getCpu().setCores(windows.cpuKerne());
				pc.getCpu().setFrequenz(windows.cpuFrequenz());
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		} else {
			PCRepository.update(pc);
			pc.setCpu(CPURepository.get(pc.getId()));
			pc.setRam(RAMRepository.get(pc.getId()));
			pc.setDrives(DriveRepository.get(pc.getId()));
			if (pc.getCpu() == null) {
				CPU cpu = new CPU();
				pc.setCpu(cpu);
			}
			if (pc.getRam() == null) { // und falls doch werden hier die Infos rausgelesen und in einem Objekt
										// gespeichert
				RAM ram = new RAM();
				pc.setRam(ram);
			}
			if (pc.getDrives().isEmpty()) {
				List<Drive> drives = new LinkedList<Drive>();
				pc.setDrives(drives);
			}
		}
		if (isWindows()) {

			int counter = 0;
			while (true) {
				try {
					pc.setRam(windows.getRamInfo(pc.getRam()));
					if (counter == 0) { // Der Teil in dieser IF-Bedingung wird nur einmal pro Minute ausgeführt
						pc.getCpu().setCores(windows.cpuKerne());
						pc.getCpu().setFrequenz(windows.cpuFrequenz());

						List<Drive> drives = windows.Festplatten(); // Hier wird auf Basis vorheriger Ergebnisse
																	// geschaut ob sich der PC schon in der Datenbank
																	// befand,
						updateDrives(drives); // auf Basis dessen wird entweder eingefügt oder geupdatet
						pc.setDrives(drives);
						pc.setName(windows.getPCName());
						if (pc.getCpu().getId() == -1) {
							CPURepository.insert(pc.getCpu(), pc.getId());
						} else {
							CPURepository.update(pc.getCpu(), pc.getId());
						}
						if (pc.getRam().getId() == -1) {
							RAMRepository.insert(pc.getRam(), pc.getId());
						} else {
							RAMRepository.update(pc.getRam(), pc.getId());
						}

					}
					pc.getCpu().getCpuUsage().setUsage(windows.cpuAuslastung()); // Hier wird die Auslastung der
																					// einzelnen Komponeten in die
																					// Datenbank geladen
					for (Drive drive : pc.getDrives()) {
						DriveRepository.insert_Drive_Usage(drive, drive.getDriveUsage());
					}
					RAMRepository.insertRamUsage(pc.getRam(), pc.getRam().getRamUsage());
					CPURepository.insertCpuUsage(pc.getCpu().getId(), pc.getCpu().getCpuUsage());

					TimeUnit.SECONDS.sleep(60);
					counter = (counter + 1) % (24 * 60); // Hier ist der Timer wann das Programm ausgeführt wird(24 * 60
															// heißt es wird einmal pr tag ausgefürhrt (In den klammern
															// stehen die Minuten))

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Fügt ein,löscht oder updated die Festplatten in der Datenbank.
	 * 
	 * @param drivesPC Die Festplatten
	 */
	private void updateDrives(List<Drive> drivesPC) { // In der folgenden Methode wird die Datenbank auf 3 Möglichkeiten
														// bearbeitet:
		List<Drive> drivesDB = DriveRepository.get(pc.getId());
		for (Drive drivePC : drivesPC) {
			for (Drive driveDB : drivesDB) {
				if (driveDB.equals(drivePC)) { // Wenn sich eine Festplatte im PC und in der Datenbank befindet dann
												// wird die Festplatte geupdatet
					drivePC.setId(driveDB.getId());
					DriveRepository.update(driveDB);
				}
			}
		}
		for (Drive drivePC : drivesPC) {
			if (!drivesDB.contains(drivePC)) { // Wenn sich die Festplatte im PC aber NICHT in der Datenbank befindet
												// wird sie eingefügt
				DriveRepository.insert(drivePC, pc.getId());
			}
		}
		for (Drive driveDB : drivesDB) {
			if (!drivesPC.contains(driveDB)) { // Wenn sich die Festplatte NICHT im PC aber in der Datenbank befindet
												// wird sie aus der Datenbank gelöscht
				DriveRepository.delete(driveDB);
			}
		}

	}

	/**
	 * Die Methode schreibt alles was das Programm ausließt in die Konsole. Wird
	 * eigentlich nur zum Bugfixen benutzt.
	 */
	public void printPC() { // Die folgende Methode ist dazu da alle Infos die in der Datenbank gespeichert
							// werden auf der Konsole auszugeben
		System.out.println("Name: " + pc.getName());
		System.out.println("");
		System.out.println("CPU");
		System.out.println("Kerne: " + pc.getCpu().getCores());
		System.out.println("Frequenz: " + pc.getCpu().getFrequenz() / 1000 + " GHz");
		System.out.println("Auslastung: " + pc.getCpu().getCpuUsage().getUsage() + " %");
		System.out.println("");
		System.out.println("RAM");
		System.out.println("Max Speicher: " + pc.getRam().getMaxRam() / 1000 + " GB");
		System.out.println("Aktuelle Auslastung: " + pc.getRam().getRamUsage().getUsage() + " %");
		System.out.println("");
		System.out.println("Festplatten");
		for (Drive drive : pc.getDrives()) {
			System.out.println("Name: " + drive.getDriveName());
			System.out.println("Buchstabe: " + drive.getDriveLetter());
			System.out.println("Typ: " + drive.getDriveType());
			System.out.println("Maximaler Speicher: " + drive.getMaxSpace() + " GB");
			System.out.println("Auslastung: " + drive.getDriveUsage().getUsage() + " %");
			System.out.println("");
		}
	}

	/**
	 * Überprüft ob das Betriebssystem Windows ist.
	 * 
	 * @return True wenn das Betreibssystem Windows ist, false falls nicht.
	 */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/**
	 * Überprüft ob das Betriebssystem MacOS ist.
	 * 
	 * @return True wenn das Betreibssystem MacOS ist, false falls nicht.
	 */
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/**
	 * Überprüft ob das Betriebssystem Unix ist.
	 * 
	 * @return True wenn das Betreibssystem Unix ist, false falls nicht.
	 */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
}
