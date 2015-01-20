/**
 * 
 */
package org.lambdamatic.example.citibikenyc.service;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.lambdamatic.example.citibikenyc.domain.BikeStation;
import org.lambdamatic.example.citibikenyc.domain.BikeStationCollection;
import org.lambdamatic.mongodb.types.geospatial.Location;

/**
 * The {@link BikeStation} Service.
 * 
 * @author Xavier Coulon
 * 
 */
@Singleton
@Startup
public class BikeStationsStorageService {

	@Inject
	private BikeStationCollection bikeStationCollection;
	
	@Inject
	private Event<BikeStation> bikeStationEvent;

	/**
	 * Inserts or updates the given {@link BikeStation} in the backend storage
	 * @param bikeStation the {@link BikeStation} 
	 */
	public void upsertBikeStation(final BikeStation bikeStation) {
		bikeStationCollection.upsert(bikeStation);
		bikeStationEvent.fire(bikeStation);
	}
	
	/**
	 * Finds all {@link BikeStation} within the polygon built after the given array of {@link Location}
	 * @param locations the polygon corner location
	 * @return the list of {@link BikeStation} within the polygon
	 */
	public List<BikeStation> findWithin(final Location[] locations) {
		return bikeStationCollection.find(s -> s.location.geoWithin(locations)).toList();
	}


}