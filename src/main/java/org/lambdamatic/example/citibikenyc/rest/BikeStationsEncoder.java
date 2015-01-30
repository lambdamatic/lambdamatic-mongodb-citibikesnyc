/**
 * 
 */
package org.lambdamatic.example.citibikenyc.rest;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;

import org.lambdamatic.example.citibikenyc.domain.BikeStation;

/**
 * @author xcoulon
 *
 */
public class BikeStationsEncoder implements Text<List<BikeStation>> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(final List<BikeStation> bikeStations) throws EncodeException {
		final JsonArrayBuilder bikeStationsArrayBuilder = Json.createArrayBuilder();
		for(BikeStation bikeStation : bikeStations) {
			final JsonObjectBuilder bikeStationObjectBuilder = Json.createObjectBuilder();
			bikeStationObjectBuilder.add("stationName", bikeStation.getStationName());
			bikeStationObjectBuilder.add("availableBikes", bikeStation.getAvailableBikes());
			bikeStationObjectBuilder.add("availableDocks", bikeStation.getAvailableDocks());
			bikeStationObjectBuilder.add("totalDocks", bikeStation.getTotalDocks());
			final JsonObjectBuilder locationBuilder = Json.createObjectBuilder();
			locationBuilder.add("latitude", bikeStation.getLocation().getLatitude());
			locationBuilder.add("longitude", bikeStation.getLocation().getLongitude());
			bikeStationObjectBuilder.add("location", locationBuilder);
			bikeStationsArrayBuilder.add(bikeStationObjectBuilder);
		}
		final String bikestations = bikeStationsArrayBuilder.build().toString();
		return bikestations;
	}

}
