package de.pp.rm.data;

/**
 * Die Datenstruktur zur Speicherung von Festplatten.
 * 
 * Was wird gespeichert: die ID, der maximale Speicher, der Laufwerksbuchstabe, die
 * Auslastung, der Name und der Typ.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class Drive {
	private int id;
	private double maxSpace;
	private char driveLetter;
	private Usage driveUsage;
	private String driveName;
	private String driveType;

	public Drive() {
		driveUsage = new Usage();
		this.id = -1;
	}

	public String getDriveType() {
		return driveType;
	}

	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}

	public String getDriveName() {
		return driveName;
	}

	public void setDriveName(String driveName) {
		this.driveName = driveName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMaxSpace() {
		return maxSpace;
	}

	public void setMaxSpace(double maxSpace) {
		this.maxSpace = maxSpace;
	}

	public char getDriveLetter() {
		return driveLetter;
	}

	public void setDriveLetter(char driveLetter) {
		this.driveLetter = driveLetter;
	}

	public Usage getDriveUsage() {
		return driveUsage;
	}

	public void setDriveUsage(Usage driveUsage) {
		this.driveUsage = driveUsage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Drive) {
			Drive d = (Drive) obj;
			return d.getDriveLetter() == driveLetter;
		}
		return false;
	}
}
