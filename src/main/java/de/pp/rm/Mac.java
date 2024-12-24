package de.pp.rm;

//import java.io.BufferedReader;
//import java.io.IOException;
/**
 * Die zukünftige MAC-Klasse mit allen Informationen.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 *
 */
public class Mac {
	// Runtime.getRuntime().exec(""); // befehl direkt ausführen ohne zu öffnen	
	/*
	 * public static void cpuFrequenz() throws IOException { BufferedReader r =
	 * execute("sysctl -n machdep.cpu.brand_string"); Erstmal nur den Befehl
	 * beachten(Freqenz in Ghz) String line = lesen(r, 16); String a =
	 * line.split("~")[1]; String b = getNumbersFromText(a);
	 * System.out.println("CPU Frequenz " + b); cpuF = b;
	 * 
	 * }
	 */
	/*
	 * public static void cpuAuslastung() throws IOException { BufferedReader r =
	 * execute("ps -A -o %cpu | awk '{s+=$1} END {print s "%"}'"); Andere Befehl:
	 * " top -1 1 | grep E "^CPU" " Cpu auslastung in % String line = lesen(r, 2);
	 * String a = line.trim(); System.out.println("CPU Auslastung " + a + "%"); cpuA
	 * = a; }
	 */

	/*
	 * public static void cpuKerne() throws IOException { BufferedReader r =
	 * execute("system_profiler SPHardwareDataType | grep Cores:"); CPU Kerne String
	 * line = lesen(r, 0); String a = line.trim(); System.out.println("CPU Kerne : "
	 * + a); cpuK = a; }
	 */

	/*
	 * public static void maxRam() throws IOException { BufferedReader r =
	 * execute("system_profiler SPHardwareDataType | grep Memory:"); Max Ram in GB
	 * String line = lesen(r, 2); String a = line.trim();
	 * System.out.println("CPU Auslastung " + a + "%"); cpuA = a; }
	 */
	/*
	 * public static void maxRam() throws IOException { BufferedReader r =
	 * execute("top -1 1 | grep E "^Phys""); Free Ram in MB String line = lesen(r,
	 * 2); String a = line.trim(); System.out.println("CPU Auslastung " + a + "%");
	 * cpuA = a; }
	 */
}