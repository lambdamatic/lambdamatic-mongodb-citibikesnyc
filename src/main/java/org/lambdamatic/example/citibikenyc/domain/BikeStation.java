/**
 * 
 */
package org.lambdamatic.example.citibikenyc.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.lambdamatic.mongodb.annotations.Document;
import org.lambdamatic.mongodb.annotations.DocumentField;
import org.lambdamatic.mongodb.annotations.DocumentId;
import org.lambdamatic.mongodb.types.geospatial.Location;

/**
 * A Bike Station document
 * 
 * @author Xavier Coulon
 *
 */
@XmlRootElement
@Document(collection="bikestations")
public class BikeStation { 
	
	@DocumentId 
	private String id;
	
	@DocumentField
	private String stationName;
	
	@DocumentField
	private int availableDocks;

	@DocumentField
	private int totalDocks;

	@DocumentField
	private int availableBikes;
	
	@DocumentField
	private Location location;
	
	@DocumentField
	private BikeStationStatus status;

	@DocumentField
	private boolean testStation;

	@DocumentField
	private Date executionTime;
	
	/**
	 * Empty Constructor
	 */
	public BikeStation() {
		
	}
	
	/**
	 * Full constructor
	 * @param id
	 * @param stationName
	 * @param availableDocks
	 * @param totalDocks
	 * @param availableBikes
	 * @param latitude
	 * @param longitude
	 * @param status
	 * @param testStation
	 * @param executionTime
	 */
	public BikeStation(final String id, final String stationName, final int availableDocks, final int totalDocks, final int availableBikes,
			final Double latitude, final Double longitude, final BikeStationStatus status, final boolean testStation, final Date executionTime) {
		super();
		this.id = id;
		this.stationName = stationName;
		this.availableDocks = availableDocks;
		this.totalDocks = totalDocks;
		this.availableBikes = availableBikes;
		this.location = new Location(longitude, latitude);
		this.status = status;
		this.testStation = testStation;
		this.executionTime = executionTime;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(final String stationName) {
		this.stationName = stationName;
	}

	public int getAvailableDocks() {
		return availableDocks;
	}

	public void setAvailableDocks(final int availableDocks) {
		this.availableDocks = availableDocks;
	}

	public int getTotalDocks() {
		return totalDocks;
	}

	public void setTotalDocks(final int totalDocks) {
		this.totalDocks = totalDocks;
	}

	public int getAvailableBikes() {
		return availableBikes;
	}

	public void setAvailableBikes(final int availableBikes) {
		this.availableBikes = availableBikes;
	}

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}

	public BikeStationStatus getStatus() {
		return status;
	}

	public void setStatus(final BikeStationStatus status) {
		this.status = status;
	}

	public boolean isTestStation() {
		return testStation;
	}
	
	public void setTestStation(final boolean testStation) {
		this.testStation = testStation;
	}

	public Date getExecutionTime() {
		return executionTime;
	}
	
	public void setExecutionTime(final Date executionTime) {
		this.executionTime = executionTime;
	}
	
	@Override
	public String toString() {
		return "Bike station " + stationName + " available docks:" + availableDocks + " / available bikes:" + availableBikes;
	}
	
	
}
