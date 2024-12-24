package de.pp.rm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.pp.rm.data.RAM;
import de.pp.rm.data.Usage;

/**
 * Die Methoden um Daten über das RAM in die Datenbank hochzuladen, zu löschen
 * und sie zu bearbeiten.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class RAMRepository {
	/**
	 * Fügt eine neue RAM in der Datenbank ein, mit all ihren Informationen.
	 * 
	 * @param ram  Der zu speichernde RAM.
	 * @param pcId Die dazugehörige Datenbank-ID des PC's.
	 */
	public static void insert(RAM ram, int pcId) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Insert into ram (maxRam,pc) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setDouble(1, ram.getMaxRam() / 1000);
			statement.setInt(2, pcId);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			int key = -1;
			if (result.next()) {
				key = result.getInt(1);
			}
			ram.setId(key);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

	/**
	 * Aktualisiert die Einträge in der Datenbank von dem bestimmten RAM.
	 * 
	 * @param ram  Das zu aktualisiernde RAM.
	 * @param pcId Die Datenbank-ID des dazugehörigen PC's.
	 */
	public static void update(RAM ram, int pcId) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Update ram set maxRam=? where pc =?");
			statement.setDouble(1, ram.getMaxRam() / 1000);
			statement.setInt(2, pcId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lädt einen RAM von der Datenbank.
	 * 
	 * @param pcId Die Datenbank-ID des dazugehörigen PC's.
	 * @return Das RAM aus der Datenbank.
	 */
	public static RAM get(int pcId) {
		Connection con = Database.connect();
		RAM ram = null;
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM ram where pc = ?");
			statement.setInt(1, pcId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				ram = new RAM();
				ram.setId(rs.getInt("id"));
				ram.setMaxRam((rs.getDouble("maxRam")));
				return ram;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
		return null;
	}

	/**
	 * Fügt die Auslastung des RAM's in die Datenbank ein.
	 * 
	 * @param ram   Das RAM zudem die Auslastung gehört.
	 * @param usage Die Auslastung selbst.
	 */
	public static void insertRamUsage(RAM ram, Usage usage) {

		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Insert into workload (workload) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setDouble(1, usage.getUsage());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			int workloadId = -1;
			if (result.next()) {
				workloadId = result.getInt(1);
			}
			PreparedStatement statement2 = con.prepareStatement("Insert into workloadRAM (workload,ram) values(?,?)");
			statement2.setInt(1, workloadId);
			statement2.setInt(2, ram.getId());
			statement2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

}
