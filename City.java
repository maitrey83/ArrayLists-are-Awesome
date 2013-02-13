/*
 * File: City.java
 * =======================================================
 * A class representing a US city.
 *
 */
public class City {
	/* The name of the city. */
	private String name;
	
	/* Where the city is located. */
	private double longitude;
	private double latitude;
	
	/**
	 * Constructs a city given the name, longitude, and latitude of that city.
	 * 
	 * @param name The name of the city.
	 * @param latitude The latitude of the city.
	 * @param longitude The longitude of the city.
	 */
	public City(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Returns the name of this city.
	 *  
	 * @return The name of this city.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the longitude of this city.
	 * 
	 * @return The longitude of this city.
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Returns the latitude of this city.
	 * 
	 * @return The latitude of this city.
	 */
	public double getLatitude() {
		return latitude;
	}
}
