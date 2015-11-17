/**
 * 
 */
package org.lambdamatic.example.citibikenyc.rest;

import java.util.List;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.lambdamatic.example.citibikenyc.domain.BikeStation;
import org.lambdamatic.example.citibikenyc.service.BikeStationsStorageService;
import org.lambdamatic.mongodb.types.geospatial.Location;

/**
 * @author xcoulon
 *
 */
@ServerEndpoint(value="/bikestations", decoders={LocationsDecoder.class}, encoders={BikeStationsEncoder.class})
public class BikeStationWebSocketEndpoint {

	@Inject 
	private BikeStationsStorageService bikeStationsStorageService;
	
	@OnMessage
	public List<BikeStation> findBikeStations(final List<Location> corners) {
		final List<BikeStation> bikeStations = bikeStationsStorageService.findWithin(corners); 
		return bikeStations;
	}

}
