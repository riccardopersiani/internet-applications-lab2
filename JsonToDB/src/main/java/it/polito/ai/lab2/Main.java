package it.polito.ai.lab2;

import java.util.Set;

import it.polito.ai.lab2.objects.*;

public class Main {

	public static void main(String[] args) {
		
		JdbcWriter jdbcWriter = new JdbcWriter();
		//JsonFileReader jfr = new JsonFileReader();
		System.out.println("Connected to db");
		
		// parsing Json file to Java objects
		JsonFileReader.readJson();
		
		// TODO ask to JsonFileReader all the data
		Set<BusLine> busLines = JsonFileReader.busLines;
		Set<BusStop> busStops = JsonFileReader.busStops;
		
		// put all the data in DB calling JdbcWriter
		for (BusStop busStop : busStops) {
			jdbcWriter.insertBusStop(busStop);
		}
		
		for (BusLine busLine : busLines) {
			jdbcWriter.insertBusLine(busLine);
		}
		
		jdbcWriter.save();
		
		jdbcWriter.close();
		
		System.out.println("done");
	}

}
