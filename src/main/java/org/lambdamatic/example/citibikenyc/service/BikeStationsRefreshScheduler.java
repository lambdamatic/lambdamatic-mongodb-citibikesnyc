package org.lambdamatic.example.citibikenyc.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

@Startup
@Singleton
public class BikeStationsRefreshScheduler {
    
	@Inject
	private Logger logger;
	
	public static final String BIKE_STATIONS_URL = "http://www.citibikenyc.com/stations/json";
	
	@EJB
	private BikeStationsRefreshService refreshService;
	
	//@PostConstruct
    //@Schedule(second="0", minute="*/2",hour="*", persistent=false)
    public void downloadContent() {
    	try {
    		final URL downloadURL = new URL(BIKE_STATIONS_URL);
    		refreshService.loadContent(new BufferedInputStream(downloadURL.openStream()));
    	} catch(IOException | ParseException e) {
    		logger.error("Failed to read content from " + BIKE_STATIONS_URL, e);
    	}
    }
	
}