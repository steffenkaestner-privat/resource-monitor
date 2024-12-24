package de.pp.rm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.pp.rm.data.PC;

/**
 * Die Methoden um Daten über den PC in die Datenbank hochzuladen und sie zu
 * bearbeiten.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class PCRepository {
	/**
	 * Fügt den PC in die Datenbank ein.
	 * 
	 * @param pc Der einzufügende PC.
	 */
	public static void insert(PC pc) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Insert into pc (name) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, pc.getName());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			int key = -1;
			if (result.next()) {
				key = result.getInt(1);
			}
			pc.setid(key);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

	/**
	 * Um einen PC zu updaten
	 * 
	 * @param pc der zu updatende PC.
	 */
	public static void update(PC pc) {

		Connection con = Database.connect();

		try {
			PreparedStatement statement = con.prepareStatement("Update pc set name = ? where id = ?");
			statement.setString(1, pc.getName());
			statement.setInt(2, pc.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

	/**
	 * Den PC aus der Datenbank zu laden.
	 * 
	 * @param name Der Name des zu ladenden PC's.
	 * @return den PC
	 */
	public static PC get(String name) {
		Connection con = Database.connect();
		PC pc = null;
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM pc where name = ?");
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				pc = new PC();
				pc.setid(rs.getInt("id"));
				pc.setName(rs.getString("name"));
				return pc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
		return null;
	}

}
