package de.pp.rm.data;
/**
 * Die Datenstruktur zur Speicherung von RAM.
 * 
 * Es wird gespeichert: die ID, das maximale Nutzung und die Auslastung.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class RAM {
	private int id;
	private double maxRam;
	private Usage ramUsage;
	
	public RAM() {
		ramUsage =new Usage();
		this.id=-1;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMaxRam() {
		return maxRam;
	}

	public void setMaxRam(double maxRam) {
		this.maxRam = maxRam;
	}

	public Usage getRamUsage() {
		return ramUsage;
	}

	public void setRamUsage(Usage ramUsage) {
		this.ramUsage = ramUsage;
	}
}
