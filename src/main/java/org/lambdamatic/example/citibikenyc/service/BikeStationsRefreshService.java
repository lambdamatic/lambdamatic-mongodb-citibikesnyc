package org.lambdamatic.example.citibikenyc.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.lambdamatic.example.citibikenyc.domain.BikeStation;
import org.lambdamatic.example.citibikenyc.domain.BikeStationStatus;
import org.lambdamatic.mongodb.types.geospatial.Location;
import org.slf4j.Logger;

/**
 * The {@link BikeStation} refresh service, ie, a {@link Schedule}d EJB that downloads every 5 minutes the update
 * bikestation data.
 * 
 * @author xcoulon
 *
 */
@Startup
@Singleton
public class BikeStationsRefreshService {

	@Inject
	private Logger logger;

	@EJB
	private BikeStationsStorageService storageService;

	/** ExecutionTime JSON value parser. eg: 2014-08-13 12:37:02 PM */
	private static final SimpleDateFormat executionTimeValueParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

	/**
	 * Parse the given {@code contentStream} and sends each unmarshalled instance of {@link BikeStation} over the wire
	 * to be processed by the interested service.
	 * 
	 * @param contentStream
	 *            the {@link InputStream} to parse
	 * @return a {@link Future} holding the number of {@link BikeStation} that were unmarshalled from the given
	 *         {@link contentStream}
	 * @throws ParseException
	 */
	@SuppressWarnings("incomplete-switch")
	@Asynchronous
	public Future<Integer> loadContent(final InputStream contentStream) throws ParseException {
		logger.debug("Processing new stream...");
		int counter = 0;
		final JsonParser jsonParser = Json.createParser(contentStream);
		boolean readingStations = false;
		BikeStation currentStation = null;
		Date executionTime = null;
		while (jsonParser.hasNext()) {
			final JsonParser.Event event = jsonParser.next();
			switch (event) {
			case KEY_NAME:
				final String keyName = jsonParser.getString();
				if (keyName.equals("executionTime")) {
					// move to the next token
					jsonParser.next();
					executionTime = executionTimeValueParser.parse(jsonParser.getString());
				} else if (keyName.equals("stationBeanList")) {
					readingStations = true;
				} else if (currentStation != null) {
					// move to the next token
					final JsonParser.Event valueEvent = jsonParser.next();
					switch (keyName) {
					case "availableBikes":
						currentStation.setAvailableBikes(jsonParser.getInt());
						break;
					case "availableDocks":
						currentStation.setAvailableDocks(jsonParser.getInt());
						break;
					case "id":
						currentStation.setId(jsonParser.getString());
						break;
					case "latitude":
						if(currentStation.getLocation() == null) {
							currentStation.setLocation(new Location());
						}
						currentStation.getLocation().setLatitude(jsonParser.getBigDecimal().doubleValue());
						break;
					case "longitude":
						if(currentStation.getLocation() == null) {
							currentStation.setLocation(new Location());
						}
						currentStation.getLocation().setLongitude(jsonParser.getBigDecimal().doubleValue());
						break;
					case "stationName":
						currentStation.setStationName(jsonParser.getString());
						break;
					case "testStation":
						if (valueEvent == Event.VALUE_TRUE) {
							currentStation.setTestStation(true);
						} else {
							currentStation.setTestStation(false);
						}
						break;
					case "statusKey":
						currentStation.setStatus(BikeStationStatus.valueOf(jsonParser.getInt()));
						break;
					case "totalDocks":
						currentStation.setTotalDocks(jsonParser.getInt());
						break;
					}
				}
				break;
			case START_OBJECT:
				if (readingStations) {
					currentStation = new BikeStation();
					currentStation.setExecutionTime(executionTime);
				}
				break;
			case END_OBJECT:
				if (readingStations) {
					storageService.upsertBikeStation(currentStation);
					counter++;
					currentStation = null;
				}
				break;
			case END_ARRAY:
				if (currentStation == null) {
					readingStations = false;
				}
			}
		}
		logger.info("Loaded/Updates {} bikestations", counter);

		return new AsyncResult<>(new Integer(counter));
	}
}