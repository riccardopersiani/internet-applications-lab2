package it.polito.ai.lab2;

import java.nio.file.*;
import java.sql.*;
import java.util.*;

import it.polito.ai.lab2.objects.*;

public class JdbcWriter {

	private Connection connection;
	private String insertBusLineStr = "INSERT INTO BusLine(line, description) values (?, ?)";
	private String insertBusStopStr = "INSERT INTO BusStop(id, name, lat, lng) values (?, ?, ?, ?)";
	private String insertBusLineStopStr = "INSERT INTO BusLineStop(stopId, lineId, sequenceNumber) values (?, ?, ?)";
	private String deleteAll = "DELETE FROM BusLineStop; DELETE FROM BusLine; DELETE FROM BusStop";

	private PreparedStatement busLineInsertStmt;
	private PreparedStatement busStopInsertStmt;
	private PreparedStatement busLineStopInsertStmt;

	public JdbcWriter() {
		try {
			Class.forName("org.postgresql.Driver");
			
			String server = null;
			try {
				List<String> lines = Files.readAllLines(Paths.get(JdbcWriter.class.getClassLoader().getResource("db_ip.txt").toURI().toString().substring(6)));
				server = lines.get(0);
			} catch (Exception e) {
				server = "localhost";
			}

			connection = DriverManager.getConnection("jdbc:postgresql://" + server + ":5432/trasporti", "postgres",
					"ai-user-password");
			// set auto commit to false so that transactions are used
			connection.setAutoCommit(false);
			// clear the three tables
			connection.createStatement().execute(deleteAll);
			busLineInsertStmt = connection.prepareStatement(insertBusLineStr);
			busStopInsertStmt = connection.prepareStatement(insertBusStopStr);
			busLineStopInsertStmt = connection.prepareStatement(insertBusLineStopStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Always insert all the stops before adding the bus lines, because of
	 * foreign keys
	 */
	public void insertBusLine(BusLine busLine) {
		try {
			busLineInsertStmt.setString(1, busLine.getLine());
			busLineInsertStmt.setString(2, busLine.getDescription());
			busLineInsertStmt.executeUpdate();
			insertBusLineStop(busLine);
		} catch (SQLException e) {
			throw new RuntimeException("insert bus line failed: " + e.getMessage());
		}

	}

	void insertBusLineStop(BusLine busLine) {
		// TODO Modificare qui il sequence number? 
		int seqNumber = 0;
		try {
			for (String stopId : busLine.getStops()) {

				busLineStopInsertStmt.setString(1, stopId);
				busLineStopInsertStmt.setString(2, busLine.getLine());
				busLineStopInsertStmt.setInt(3, seqNumber);
				busLineStopInsertStmt.executeUpdate();
				seqNumber++;
			}
		} catch (SQLException e) {
			throw new RuntimeException("insert bus line stop failed: " + e.getMessage());
		}
	}

	public void insertBusStop(BusStop busStop) {
		try {
			busStopInsertStmt.setString(1, busStop.getId());
			busStopInsertStmt.setString(2, busStop.getName());
			busStopInsertStmt.setDouble(3, busStop.getLat());
			busStopInsertStmt.setDouble(4, busStop.getLng());
			busStopInsertStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("insert bus stop failed: " + e.getMessage());
		}
	}

	/**
	 * to save after some calls to the insert functions
	 */
	public void save() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	/**
	 * to close the connection to the db
	 */
	public void close() {
		try {
			busLineInsertStmt.close();
			busStopInsertStmt.close();
			busLineStopInsertStmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
