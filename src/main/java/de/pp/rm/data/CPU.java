package de.pp.rm.data;
/**
 * Die Datenstruktur zur Speicherung von CPU's.
 * 
 * Es wird gespeichert: die ID, die CPU-Frequenz, die CPU-Auslastung und die Kerne.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class CPU {
	private int id;
	private double Frequenz;
	private Usage cpuUsage;
	private int cores;

	public CPU() {
		cpuUsage = new Usage();
		this.id=-1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getFrequenz() {
		return Frequenz;
	}

	public void setFrequenz(double frequenz) {
		Frequenz = frequenz;
	}

	public Usage getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Usage cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}
}
