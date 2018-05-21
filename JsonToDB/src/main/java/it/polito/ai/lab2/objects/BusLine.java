/**
 * 
 */
package it.polito.ai.lab2.objects;

import java.util.List;

public class BusLine {
	
	private String line;
	private String description;
	private List<String> stops;
	
	public BusLine(String line, String description, List<String> stops) {
		this.line = line;
		this.description = description;
		this.stops = stops;
	}
	
	public String getLine() {
		return line;
	}
	
	public String getDescription() {
		return description;
	}

	public List<String> getStops() {
		return stops;
	}
}
