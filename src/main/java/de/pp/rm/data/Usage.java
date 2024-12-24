package de.pp.rm.data;

import java.time.LocalDateTime;
/**
 * Die Datenstruktur zur Speicherung von der Auslastung.
 * 
 * Es wird gespeichert: die ID, die aktuelle Zeit und die Auslastung.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class Usage {
	private int id;
	private LocalDateTime timestamp;
	private double usage;

	public Usage() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
		timestamp = LocalDateTime.now();
	}
}
