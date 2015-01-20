/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.lambdamatic.example.citibikenyc.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.lambdamatic.example.citibikenyc.domain.BikeStation;
import org.lambdamatic.example.citibikenyc.service.BikeStationsStorageService;
import org.lambdamatic.mongodb.types.geospatial.Location;

/**
 * @author Xavier Coulon <xcoulon@redhat.com>
 *
 */
@RequestScoped
@Path("/bikestations")
public class BikeStationEndpoint {

	@Inject 
	private BikeStationsStorageService bikeStationsStorageService;
	
	/**
	 * @param id
	 * @return
	 */
	@GET
	@Produces("application/json")
	public Response findWithin(@QueryParam("location") final Location[] locations) {
		final List<BikeStation> bikeStations = bikeStationsStorageService.findWithin(locations); 
		if (bikeStations.isEmpty()) {
			return Response.status(Status.NO_CONTENT).build();
		} 
		return Response.ok(bikeStations).build();
	}

}
