package de.pp.rm.data;

import java.util.List;

/**
 * Die Datenstruktur zur Speicherung von Festplatten.
 * 
 * Es wird gespeichert: die ID, der Name, die CPU, das RAM und die Festplatten.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class PC {
	private int id;
	private String name;
	private CPU cpu;
	private RAM ram;
	private List<Drive> drives;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public RAM getRam() {
		return ram;
	}

	public void setRam(RAM ram) {
		this.ram = ram;
	}

	public List<Drive> getDrives() {
		return drives;
	}

	public void setDrives(List<Drive> drives) {
		this.drives = drives;
	}

	public void addDrive(Drive drive) {
		drives.add(drive);
	}

	public void removeDrive(Drive drive) {
		drives.remove(drive);
	}
}
