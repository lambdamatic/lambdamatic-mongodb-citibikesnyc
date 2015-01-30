/**
 * 
 */
package org.lambdamatic.example.citibikenyc.rest;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.websocket.DecodeException;
import javax.websocket.Decoder.Text;
import javax.websocket.EndpointConfig;

import org.lambdamatic.mongodb.types.geospatial.Location;

/**
 * @author Xavier Coulon 
 *
 */
public class LocationsDecoder implements Text<List<Location>> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	/**
	 * Decode the incoming JSON message which is expected to have the following form:
	 * <pre>
	 * { [ {'latitude':&lt;value&gt;, 'longitude':&lt;value&gt;}, 
	 *     {'latitude':&lt;value&gt;, 'longitude':&lt;value&gt;},
	 *     {'latitude':&lt;value&gt;, 'longitude':&lt;value&gt;},
	 *     {'latitude':&lt;value&gt;, 'longitude':&lt;value&gt;}
	 *   ] } 
	 * }
	 * </pre>
	 * @param message the incoming message
	 */
	@Override
	public List<Location> decode(final String message) throws DecodeException {
		final List<Location> locations = new ArrayList<>();
		final JsonArray jsonLocationsArray = Json
		        .createReader(new StringReader(message)).readArray();
		for(Iterator<JsonValue> iterator = jsonLocationsArray.iterator(); iterator.hasNext();) {
			final JsonObject locationObject = (JsonObject) iterator.next();
			final JsonNumber latitudeValue = (JsonNumber) locationObject.get("latitude");
			final JsonNumber longitudeValue = (JsonNumber) locationObject.get("longitude");
			final Location location = new Location(latitudeValue.doubleValue(), longitudeValue.doubleValue());
			locations.add(location);
		}
		return locations;
	}

	/**
	 * Assumes that the incoming message can be decoded by this {@link LocationsDecoder}. 
	 * @param message the incoming message
	 * @return {@code true}.
	 */
	@Override
	public boolean willDecode(final String message) {
		return true;
	}

}
