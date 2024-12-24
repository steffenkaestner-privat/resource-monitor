package de.pp.rm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import de.pp.rm.data.Drive;
import de.pp.rm.data.Usage;

/**
 * Die Klasse dient dazu alle Festplatteninformationen die das Programm über
 * eine Festplatte ausließt in die Datenbank hochzuladen. Hierbei und werden
 * Prepared Statements an die Datenbank geschickt.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class DriveRepository {

	/**
	 * Es wird eine Festplatte mit all ihren Infos in die Datenbank hochgeladen.
	 * 
	 * @param drive Die Festplatte die hochgeladen werden soll.
	 * @param pcId  Die ID die der PC in der Datenbank zugewiesen bekommen hat.
	 */
	public static void insert(Drive drive, int pcId) {

		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement(
					"Insert into drive (name, driveLetter, type, maxSpace,pc) values (?,?,?,?,?)", // Das eigentliche
																									// Statement
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, drive.getDriveName());
			statement.setString(2, String.valueOf(drive.getDriveLetter()));
			statement.setString(3, drive.getDriveType());
			statement.setDouble(4, drive.getMaxSpace());
			statement.setInt(5, pcId);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			int key = -1; // Die Standart ID solange die Festplatte nicht in der Datenbank ist ist -1
			if (result.next()) {
				key = result.getInt(1);
			}
			drive.setId(key);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

	/**
	 * Es wird eine Festplatte aktualisiert.
	 * 
	 * @param drive Die Festplatte die geupdated werden soll
	 */
	public static void update(Drive drive) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con
					.prepareStatement("Update drive set name=?,type=?, maxSpace =? where driveLetter=?");
			statement.setString(1, drive.getDriveName());
			statement.setString(2, drive.getDriveType());
			statement.setDouble(3, drive.getMaxSpace());
			statement.setString(4, String.valueOf(drive.getDriveLetter()));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Festplatte wird gelöscht.
	 * 
	 * @param drive Die zu löschende Festplatte.
	 */
	public static void delete(Drive drive) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("DELETE from drive where id =?");
			statement.setInt(1, drive.getId());
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}

	/**
	 * Lädt alle Festplatten aus der Datenbank vom jeweiligen PC.
	 * 
	 * @param pcId Die ID vom jeweiligen Rechner in der Datenbank
	 * @return eine Liste mit allen Festplatten in dem Rechner
	 */
	public static List<Drive> get(int pcId) {
		Connection con = Database.connect();
		List<Drive> drives = new LinkedList<Drive>();
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM drive where pc = ?");
			statement.setInt(1, pcId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Drive drive = new Drive();
				drive.setId(rs.getInt("id"));
				drive.setDriveLetter(rs.getString("driveLetter").charAt(0));
				drive.setDriveName(rs.getString("name"));
				drive.setDriveType(rs.getString("type"));
				drive.setMaxSpace(rs.getDouble("maxSpace"));
				drives.add(drive);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
		return drives;
	}

	/**
	 * Speichert die Nutzung in Prozent der jeweiligen Festplatte in der Datenbank.
	 * 
	 * @param drive Die Festplatte welche der Usage angehört
	 * @param usage Die Auslastung als Prozentzahl
	 */
	public static void insert_Drive_Usage(Drive drive, Usage usage) {

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

			PreparedStatement statement2 = con
					.prepareStatement("Insert into workloadDrive (workload,drive) values (?,?)");
			statement2.setInt(1, workloadId);
			statement2.setInt(2, drive.getId());
			statement2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

}
