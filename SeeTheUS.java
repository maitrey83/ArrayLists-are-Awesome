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
	public static final int APPLICATION_WIDTH = 100;
	public static final int APPLICATION_HEIGHT = 70;
	
	/* The viewpoint coordinates - the minimum and maximum longitude
	 * and latitude.
	 */
	private static final double MIN_LONGITUDE = -180;
	private static final double MAX_LONGITUDE = -60;
	
	private static final double MIN_LATITUDE = +15;
	private static final double MAX_LATITUDE = +75;
	
	/* For the heat map, the initial radius, minimum radius, and decay rate. */
	private static final double INITIAL_RADIUS = 75;
	private static final double DECAY_RATE = 0.999;
	private static final double MIN_RADIUS = 5;
	
	/* The name of the cities file. */
	private static final String CITIES_FILE = "us-cities.txt";
	
	public void run() {
		/* Everything is more awesome on a black background! */
		setBackground(Color.BLACK);
		
		/* Load in the list of US cities. */
		ArrayList<City> cities = readUSCities();
		
		/* Draw the iteratively-generated heat map. */
		visualizeTheUS(cities);
	}
	
	/**
	 * Reads in a list of US cities.
	 * 
	 * @return A list of all US cities.
	 */
	private ArrayList<City> readUSCities() {
		try {
			ArrayList<City> result = new ArrayList<City>();
			BufferedReader br = new BufferedReader(new FileReader(CITIES_FILE));
			
			while (true) {
				/* Read the three pieces of the city from the file. */
				String name = br.readLine();
				String latitude = br.readLine();
				String longitude = br.readLine();
				
				/* If we ran out of data, we're done. */
				if (longitude == null)
					break;
				
				/* Create the city and add it to the result list.  Note the
				 * call to Double.parseDouble to convert the string representations
				 * of the longitude and latitude into doubles.
				 */
				City toAdd = new City(name,
						              Double.parseDouble(longitude),
						              Double.parseDouble(latitude));
				result.add(toAdd);
			}
			
			br.close();
			return result;			
		} catch (IOException e) {
			throw new ErrorException(e);
		}
	}
	
	/**
	 * Iteratively draws a heat map by picking random points and computing the distance from
	 * that point to the nearest US city.  This method does not return.
	 * 
	 * @param cities The cities in the US.
	 */
	private void visualizeTheUS(ArrayList<City> cities) {	
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {				
				/* Pick a point to draw. */
				GPoint pt = new GPoint(xCoordinateToLongitude(x),
						               yCoordinateToLatitude(y));

				/* Determine how close it is to a US city. */
				double distance = distanceToNearestCity(pt, cities);

				/* Draw a point based on how close we are. */
				drawPointAtCoordinate(x, y, 1, getColorForDistance(distance));
			}
		}
	}
	
	/**
	 * Chooses a random longitude/latitude point in the bounding box for US cities.
	 * 
	 * @return A random longitude/latitude point in the bounding box for US cities.
	 */
	private GPoint getRandomPoint() {
		RandomGenerator rgen = RandomGenerator.getInstance();
		return new GPoint(rgen.nextDouble(MIN_LONGITUDE, MAX_LONGITUDE),
				          rgen.nextDouble(MIN_LATITUDE, MAX_LATITUDE));
	}
	
	/**
	 * Given a longitude/latitude point and a list of US cities, returns how close the given
	 * point is to a US city.
	 * 
	 * @param pt The point to check.
	 * @param cities A list of US cities.
	 * @return The distance to the nearest city.
	 */
	private double distanceToNearestCity(GPoint pt, ArrayList<City> cities) {
		/* Track our initial guess, which is originally as large as possible. */
		double bestDistance = Double.POSITIVE_INFINITY;
		for (int i = 0; i < cities.size(); i++) {
			/* Update the distance to be the best we've seen so far. */
			bestDistance = Math.min(bestDistance, distanceToCity(pt, cities.get(i)));
		}
		return bestDistance;
	}
	
	/**
	 * Given a point and a city, returns an approximation of the distance from that
	 * point to that city.
	 * 
	 * @param pt The point in question.
	 * @param city The city.
	 * @return How far apart they are.
	 */
	private double distanceToCity(GPoint pt, City city) {
		/* Using Euclidean geometry, determine the distance. */
		double dx = pt.getX() - city.getLongitude();
		double dy = pt.getY() - city.getLatitude();
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Draws a block of the given color centered at the appropriate longitude/latitude pair.
	 * 
	 * @param longitude The longitude at which to draw the point.
	 * @param latitude The latitude at which to draw the point.
	 * @param radius The radius of the block.
	 * @param color What color to use.
	 */
	private void drawPointAtCoordinate(double longitude, double latitude, double radius, Color color) {
		/* Determine the x/y coordinates from the longitude and latitude. */
		double x = longitudeToXCoordinate(longitude);
		double y = latitudeToYCoordinate(latitude);

		/* Create a point at that location. */
		GRect point = new GRect(x, y, 1, 1)
		point.setFilled(true);
		point.setColor(color);
		add(point);
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
		double scaleFactor = (MAX_LATITUDE - MIN_LATITUDE) / getWidth();
		return y * scaleFactor + MIN_LATITUDE;
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
		//return new Color(intensity, intensity, intensity);
		return Color.GREEN;
	}
}
