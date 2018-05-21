package it.polito.ai.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import it.polito.ai.lab2.objects.BusLine;
import it.polito.ai.lab2.objects.BusStop;

// Reads a JSON object or an array structure from an input source.
public class JsonFileReader {

	static Set<BusLine> busLines = new HashSet<BusLine>();
	static Set<BusStop> busStops = new HashSet<BusStop>();

	 static void readJson(){
		File jsonInputFile = new File("../linee.json");
		InputStream is;

		try{
			is = new FileInputStream(jsonInputFile);
			// Create JsonReader from Json.
			JsonReader reader = Json.createReader(is);
			// Get the JsonObject structure from JsonReader.
			JsonObject jsonObj = reader.readObject();

			// BUS LINES
			// read lines array
			JsonArray linesArr = jsonObj.getJsonArray("lines");
			System.out.println("Lines:");

			// read single line element from lines list
			for(int i = 0; i < linesArr.size(); i++){

				JsonObject elementObj = linesArr.getJsonObject(i);

				// read line data
				System.out.println("\nLine: " + elementObj.getString("line"));
				String line = elementObj.getString("line");

				// read desc data
				System.out.println("Desc: " + elementObj.getString("desc"));
				String desc = elementObj.getString("desc");

				// read stops array
				JsonArray stopsArr = elementObj.getJsonArray("stops");
				System.out.print("Stops: ");

				// List perché le fermate si ripetono e non sono uniche
				List<String> stops = new ArrayList<String>();

				// read stop data
				for(JsonValue value : stopsArr){	
					System.out.print(value.toString().replaceAll("\"", "") + ", ");	
					stops.add(value.toString().replaceAll("\"", ""));
				}
				BusLine bl = new BusLine(line, desc, stops);
				busLines.add(bl);
			}

			 //BUS STOPS
			// read stops array
			JsonArray busStopsArr = jsonObj.getJsonArray("stops");
			System.out.println("Stops:");

			// read single stop element from lines list
			for(int i = 0; i < busStopsArr.size(); i++){

				JsonObject busElementObj = busStopsArr.getJsonObject(i);

				// read id data
				System.out.println("\nId: " + busElementObj.getString("id"));
				String id = busElementObj.getString("id");

				// read desc data
				System.out.println("Name: " + busElementObj.getString("name"));
				String name = busElementObj.getString("name");

				// reat latLng data
				JsonArray busLatLngArr = busElementObj.getJsonArray("latLng");
				// List perché le fermate si ripetono e non sono uniche
				List<String> latLng = new ArrayList<String>();

				// read stop data
				for(JsonValue value : busLatLngArr){		
					latLng.add(value.toString().replaceAll("\"", ""));
				}
				double lat = Double.parseDouble(latLng.get(0));
				double lng = Double.parseDouble(latLng.get(1));
				
				System.out.print("Latitute: " + lat + " ");
				System.out.println("Longitude: " + lng);
				
				BusStop bs = new BusStop(id, name, lat, lng);
				busStops.add(bs);
			}


		} catch (FileNotFoundException e) {
			System.out.println("File not found error.\n");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
