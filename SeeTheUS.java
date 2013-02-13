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
		ArrayList<City> cities = readUSCities();
		visualizeTheUS(cities);
	}
	
	private ArrayList<City> readUSCities() {
		ArrayList<City> result = new ArrayList<City>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(CITIES_FILE));
			
			while (true) {
				String name = br.readLine();
				String latitude = br.readLine();
				String longitude = br.readLine();
				
				if (name == null || latitude == null || longitude == null) {
					break;
				}
				
				City city = new City(name, latitude, longitude);
				result.add(city);
			}
		}
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
