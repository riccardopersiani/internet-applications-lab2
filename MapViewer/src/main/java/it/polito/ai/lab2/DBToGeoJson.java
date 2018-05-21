package it.polito.ai.lab2;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.*;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import it.polito.ai.lab2.entities.BusLine;
import it.polito.ai.lab2.entities.BusStop;

@WebServlet("/getBusStops")
public class DBToGeoJson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Get the data from the Database and parse them in a GeoJSON format
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Retrieve the selected busLine
		String line = request.getParameter("line");
		
		// Perform the query into the DB
		Query<BusLine> query = ((Session) request.getAttribute("session")).createQuery("From BusLine l where l.line=:line", BusLine.class);
		query.setParameter("line", line);
		
		// Retrieve data query result. It is just one
		BusLine busLine = (BusLine) query.list().get(0);
		List<BusStop> busStopList = busLine.getStops();

		// Parsing of the stops
		JSONObject geoJSON = busStopsToGeoJson(busStopList);
		JSONArray lineString = getBusLinePath(busStopList);
		
		// Add the GeoJson to the session
		request.getSession().setAttribute("jsonBusStops", geoJSON.toString());
		request.getSession().setAttribute("jsonBusLine", lineString.toString());
		
		// Forward the request to the page that shows the map
		RequestDispatcher req=request.getRequestDispatcher("/mapPage.jsp");
		req.forward(request, response); 
	}

	protected JSONObject busStopsToGeoJson(List<BusStop> busStopList) {
		JSONObject root = new JSONObject();
		root.put("type", "FeatureCollection");
		
		JSONArray features = new JSONArray();
		for (BusStop stop : busStopList) {
			// Create coordinate object and set latitude and longitude
			JSONArray coordinates = new JSONArray();
			coordinates.put(stop.getLongitude());
			coordinates.put(stop.getLatitude());
			
			// Create and fill up the geometry object with type and coordinates
			JSONObject geometry = new JSONObject();
			geometry.put("type", "Point");
			geometry.put("coordinates", coordinates);
			
			// Add the list of lines
			JSONArray lines = new JSONArray();
			for (BusLine bl: stop.getLines()){
				lines.put(bl.getLine());
			}
			
			// Create and fill up the properties object
			JSONObject properties = new JSONObject();
			properties.put("busStopId", stop.getId());
			properties.put("busStopName", stop.getName());
			properties.put("lines", lines);
			
			// Create and fill up the bus stop object with type, geometry, id, properties and lines
			JSONObject feature = new JSONObject();
			feature.put("type", "Feature");
			feature.put("geometry", geometry);
			feature.put("properties", properties);
			
			// Add the single feature to the feature collection
			features.put(feature);
		}
		
		// Add every feature to the root
		root.put("features", features);
		return root;
	}
	
	public JSONArray getBusLinePath(List<BusStop> busStopList) {
		JSONArray root = new JSONArray();
		
		JSONArray coordinates = new JSONArray();
		for (BusStop stop : busStopList) {
			// Create coordinates array and fill in with the list of bust stops
			JSONArray latLong = new JSONArray();
			latLong.put(stop.getLongitude());
			latLong.put(stop.getLatitude());
			
			// Add the line segment to the coordinates array
			coordinates.put(latLong);
		}
		
		JSONObject geometry = new JSONObject();
		geometry.put("type", "LineString");
		geometry.put("coordinates", coordinates);
		
		root.put(geometry);
		return root;
	}
}
