/*
 * File: SeeTheUS.java
 * ===============================================================
 * A program to visualize the United States based on US Census
 * records from 2000.
 */

import acm.program.*;
import acm.util.*;
import acm.graphics.*;
import java.util.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;

public class SeeTheUS extends GraphicsProgram {
	/* Make the window large so that we can see more detail. */
	public static final int APPLICATION_WIDTH = 1000;
	public static final int APPLICATION_HEIGHT = 700;
	
	/* The viewpoint coordinates - the minimum and maximum longitude
	 * and latitude.
	 */
	private static final double MIN_LONGITUDE = -180;
	private static final double MAX_LONGITUDE = -60;
	
	private static final double MIN_LATITUDE = +15;
	private static final double MAX_LATITUDE = +75;
	
	/* The name of the cities file. */
	private static final String CITIES_FILE = "us-cities.txt";
	
	public void run() {	
		ArrayList<City> usCities = loadUSCities();
		
		visualizeTheUS(usCities);
	}
	
	private ArrayList<City> loadUSCities() {
		ArrayList<City> result = new ArrayList<City>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("us-cities.txt"));
			
			while (true) {
				String cityName  = br.readLine();
				String latitude  = br.readLine();
				String longitude = br.readLine();
				
				if (longitude == null) break;
				
				City city = new City(cityName,
						             Double.parseDouble(latitude),
						             Double.parseDouble(longitude));
				result.add(city);
			}
		} catch (IOException e) {
			return null;
		}
		
		return result;
	}
	
	private void visualizeTheUS(ArrayList<City> cities) {
		getGCanvas().setAutoRepaintFlag(false);
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				double longitude = xCoordinateToLongitude(x);
				double latitude  = yCoordinateToLatitude(y);
				
				double distance = distanceToNearestCity(cities, longitude, latitude);
				
				plotPixel(x, y, getColorForDistance(distance));
			}
		}
		getGCanvas().repaint();
	}
	
	private double distanceToNearestCity(ArrayList<City> cities,
			                             double longitude, double latitude) {
		double bestDistance = Double.POSITIVE_INFINITY;
		for (int i = 0; i < cities.size(); i++) {
			City curr = cities.get(i);
			double distance = distanceBetween(curr.getLongitude(),
					                          curr.getLatitude(),
					                          longitude,
					                          latitude);
			if (distance < bestDistance) {
				bestDistance = distance;
			}
		}
		return bestDistance;
	}
	
	private double distanceBetween(double x0, double y0,
			                       double x1, double y1) {
		double dx = x0 - x1;
		double dy = y0 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	private void plotPixel(double x, double y, Color color) {
		GRect pixel = new GRect(x, y, 1, 1);
		pixel.setFilled(true);
		pixel.setColor(color);
		add(pixel);
	}
	
	/**
	 * Given a raw longitude, returns the screen x coordinate where
	 * it should be displayed.
	 * 
	 * @param longitude The longitude in question.
	 * @return Where it maps to as an x coordinate.
	 */
	private double longitudeToXCoordinate(double longitude) {
		return getWidth() * (longitude - MIN_LONGITUDE) / (MAX_LONGITUDE - MIN_LONGITUDE); 
	}
	
	/**
	 * Given a raw latitude, returns the screen y coordinate where
	 * it should be displayed.
	 * 
	 * @param latitude The latitude in question.
	 * @return Where it maps to as a y coordinate.
	 */
	private double latitudeToYCoordinate(double latitude) {
		return getHeight() * (1.0 - (latitude - MIN_LATITUDE) / (MAX_LATITUDE - MIN_LATITUDE)); 
	}
	
	/**
	 * Given a raw X coordinate, returns the longitude it corresponds
	 * to.
	 * 
	 * @param x The x coordinate in question.
	 * @return The longitude it corresponds to.
	 */
	private double xCoordinateToLongitude(double x) {
		double scaleFactor = (MAX_LONGITUDE - MIN_LONGITUDE) / getWidth();
		return x * scaleFactor + MIN_LONGITUDE;
	}
	
	/**
	 * Given a raw Y coordinate, returns the latitude it corresponds
	 * to.
	 * 
	 * @param y The y coordinate in question.
	 * @return The latitude it corresponds to.
	 */
	private double yCoordinateToLatitude(double y) {
		double scaleFactor = (MAX_LATITUDE - MIN_LATITUDE) / getHeight();
		return (getHeight() - y) * scaleFactor + MIN_LATITUDE;
	}

	/**
	 * Given a distance (in degrees), returns a color based on that
	 * distance that shows the relative closeness to a US city.
	 * 
	 * @param distance The distance to the nearest US city.
	 * @return A color encoding that intensity.
	 */
	private Color getColorForDistance(double distance) {
		/* We need a function to map [0, inf) to [0, 1], so we'll
		 * use the arctangent function.
		 */
		float intensity = 1.0f - (float)(Math.atan(distance) / (Math.PI / 2.0));
		return new Color(intensity, intensity, intensity);
	}
}
