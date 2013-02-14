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
	
	/**
	 * Reads the cities file and returns a list of all of the census locations
	 * in the US.
	 * 
	 * @return A list of all cities in the United States.
	 */
	private ArrayList<City> readUSCities() {
		/* Create an ArrayList to hold the result. */
		ArrayList<City> result = new ArrayList<City>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(CITIES_FILE));
			
			while (true) {
				/* Read the name, latitude, and longitude of the file. */
				String name = br.readLine();
				String latitude = br.readLine();
				String longitude = br.readLine();
				
				/* If we've run out of cities, we're done.e */
				if (name == null || latitude == null || longitude == null) {
					break;
				}
				
				/* Construct a City object from the data and add it to the list. */
				City city = new City(name,
						             Double.parseDouble(latitude),
						             Double.parseDouble(longitude));
				result.add(city);
			}
			
			br.close();
		} catch (IOException e) {
			// Do nothing...
		}
		
		return result;
	}
	
	/**
	 * Draws a heat map of the United States, where each pixel is colored based
	 * on its proximity to the nearest US city.
	 * 
	 * @param cities The list of all cities in the US.
	 */
	private void visualizeTheUS(ArrayList<City> cities) {
		/* This is a very graphics-intensive operation.  For efficiency, we disable
		 * automatic updating of the canvas and request that we manually request
		 * updates.
		 */
		getGCanvas().setAutoRepaintFlag(false);
		
		/* Iterate across the pixels in the window and color each one
		 * appropriately.
		 */
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				/* Convert the current (x, y) location to a longitude and
				 * latitude.
				 */
				double longitude = xCoordinateToLongitude(x);
				double latitude = yCoordinateToLatitude(y);
				
				/* Determine how close the nearest city is. */
				double distance = distanceToNearestCity(longitude,
						     							latitude,
						     							cities);
				
				/* Plot an appropriately-colored pixel. */
				plotPixel(x, y, getColorForDistance(distance));
			}
			
			/* To give a sense of relative progress, output the percentage
			 * completed.
			 */
			println(100.0 * x / getWidth() + "%");
		}
		
		/* Manually redraw the window to show our result! */
		getGCanvas().repaint();
	}
	
	/**
	 * Returns the distance from the indicated point to the nearest
	 * US city.
	 * 
	 * @param longitude The point's longitude.
	 * @param latitude The point's latitude.
	 * @param cities The list of all US cities.
	 * @return The distance to the nearest US city.
	 */
	private double distanceToNearestCity(double longitude,
			                             double latitude,
			                             ArrayList<City> cities) {
		/* Store the distance to the closest US city.  Initially, we assume
		 * this is +infinity, then update it as we go.
		 */
		double bestDistance = Double.POSITIVE_INFINITY;
		
		/* Consider all US cities one at a time and keep updating our
		 * guess of the distance.
		 */
		for (int i = 0; i < cities.size(); i++) {
			City currCity = cities.get(i);
			
			/* Compute the distance from the current longitude/latitude point
			 * to this city.
			 */
			double distance = distanceBetween(longitude, latitude,
					                          currCity.getLongitude(),
					                          currCity.getLatitude());
			
			/* Update the distance appropriately. */
			if (distance < bestDistance) {
				bestDistance = distance;
			}
		}
		
		return bestDistance;
	}
	
	/**
	 * Returns the Euclidean distance between the indicated points.
	 * 
	 * @param x0 The X coordinate of the first point.
	 * @param y0 The Y coordinate of the first point.
	 * @param x1 The X coordinate of the second point.
	 * @param y1 The Y coordinate of the second point.
	 * @return The Euclidean distance between those points.
	 */
	private double distanceBetween(double x0, double y0, double x1, double y1) {
		double dx = x0 - x1;
		double dy = y0 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Plots a pixel of the given color at the specified (x, y) coordinate.
	 * 
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @param color The color of the pixel.
	 */
	private void plotPixel(double x, double y, Color color) {
		/* Create a 1x1 pixel of the given color. */
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
