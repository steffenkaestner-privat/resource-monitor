package de.pp.rm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Die Methoden um sich mit der Datenbank zu Verbinden und die Verbindung auch
 * wieder zu trennen.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class Database {
	/**
	 * Die Adresse der Datenbank.
	 */
	static String url = "jdbc:mysql://192.168.178.165/ResourceMonitor";
	/**
	 * Der Benutzername für die Datenbank.
	 */
	private static String username = "ResourceMonitor";
	/**
	 * Das Passwort für die Datenbank.
	 */
	private static String password = "ResourceMonitor";
	private static Connection connection = null;

	/**
	 * Verbindung sich mit der Datenbank.
	 * 
	 */
	public static Connection connect() {
		try {
			Properties prop = new Properties();
			prop.put("user", username);
			prop.put("password", password);
			prop.put("useSSL", "false");
			prop.put("allowPublicKeyRetrieval", "true");
			connection = DriverManager.getConnection(url, prop);
			return connection;
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	/**
	 * Trennt die Verbindung mit der Datenbank.
	 */
	public static void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}
