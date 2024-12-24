package de.pp.rm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.pp.rm.data.CPU;
import de.pp.rm.data.Usage;

/**
 * Die Methoden um Daten über die CPU in die Datenbank hochzuladen, zu löschen
 * und sie zu bearbeiten.
 * 
 * @author Kästner, Steffen <mail@steffen-kaestner.de
 * @version 1.0
 * @since 1.0
 *
 */
public class CPURepository {
	/**
	 * Fügt eine neue Cpu in der Datenbank mit all ihren Informationen ein.
	 * 
	 * @param cpu  Die CPU welche die Informationen enthält welche in der Datenbank
	 *             gespeichert werden sollen.
	 * @param pcId Die Datenbank-ID des dazugehörigen PC's.
	 */
	public static void insert(CPU cpu, int pcId) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Insert into cpu (frequency,cores,pc) values (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setDouble(1, cpu.getFrequenz() / 1000);
			statement.setInt(2, cpu.getCores());
			statement.setInt(3, pcId);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			int key = -1;
			if (result.next()) {
				key = result.getInt(1);
			}

			cpu.setId(key);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}

	/**
	 * Aktualisiert die Einträge in der Datenbank von der bestimmten Cpu.
	 * 
	 * @param cpu  Die zu aktualisierende Cpu.
	 * @param pcId Die Datenbank-ID des dazugehörigen PC's.
	 */
	public static void update(CPU cpu, int pcId) {
		Connection con = Database.connect();
		try {
			PreparedStatement statement = con.prepareStatement("Update cpu set frequency=?,cores=?  where pc =?");
			statement.setDouble(1, cpu.getFrequenz() / 1000);
			statement.setInt(2, cpu.getCores());
			statement.setInt(3, pcId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt dir die CPU aus der Datenbank zurück.
	 * 
	 * @param pcId Die Datenbank-ID des dazugehörigen PC's.
	 * @return die CPU mit allen Informationen.
	 */
	public static CPU get(int pcId) {
		Connection con = Database.connect();
		CPU cpu = null;
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM cpu where pc = ?");
			statement.setInt(1, pcId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				cpu = new CPU();
				cpu.setId(rs.getInt("id"));
				cpu.setCores(rs.getInt("cores"));
				cpu.setFrequenz(rs.getDouble("frequency"));
				return cpu;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
		return null;
	}

	/**
	 * Fügt die Auslastung der CPU in die Datenbank ein.
	 * 
	 * @param cpuId Die Datenbank-ID des dazugehörigen PC's.
	 * @param usage Die Auslastung der CPU.
	 */
	public static void insertCpuUsage(int cpuId, Usage usage) {

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
			PreparedStatement statement2 = con.prepareStatement("Insert into workloadCPU (workload,cpu) values (?,?)");
			statement2.setInt(1, workloadId);
			statement2.setInt(2, cpuId);
			statement2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.disconnect();
		}
	}
}