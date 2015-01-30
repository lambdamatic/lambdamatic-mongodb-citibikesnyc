$.ready(new function() {

	var map = L.map('map')
	map.setView([ 40.782833, -73.965033 ], 14);

	L.tileLayer(
					'https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png',
					{
						maxZoom : 15,
						minZoom : 12,
						attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
								+ '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
								+ 'Imagery © <a href="http://mapbox.com">Mapbox</a>',
						id : 'xcoulon.jnlla4f1',
					}).addTo(map);

	var markers = {};

	function getBikeStationsForCurrentMapView() {
		var locations = new Array(4);
		locations[0] = toLocation(map.getBounds().getSouthWest());
		locations[1] = toLocation(map.getBounds().getNorthWest());
		locations[2] = toLocation(map.getBounds().getNorthEast());
		locations[3] = toLocation(map.getBounds().getSouthEast());
		var message = JSON.stringify(locations);
		console.log("Sending " + message);
		webSocket.send(message);
	}

	function toLocation(location) {
		var result = new Object();
		result.latitude = location.lat;
		result.longitude = location.lng;
		return result;
	}
	
	var webSocketURI = "ws://" + window.location.host + "/" + window.location.pathname + "/bikestations";
	var webSocket = new WebSocket(webSocketURI);
	
	webSocket.onopen = function() {
		console.log("Web Socket is connected");
		map.on('move', getBikeStationsForCurrentMapView);
		// map is already loaded, so we should display the bikestations for the current map view without
		// waiting for a move event
		getBikeStationsForCurrentMapView();
	};

	webSocket.onclose = function() {
		console.log("Connection is closed...");
	};
	
	webSocket.onmessage = function(evt) {
		var bikestations = JSON.parse(evt.data);
		for (var index in bikestations) {
			var bikestation = bikestations[index];
			var location = "" + bikestation.location.latitude + ","
				+ bikestation.location.longitude;
			if (!(location in markers)) {
				var marker = L.marker(
						[ bikestation.location.latitude,
								bikestation.location.longitude ])
						.addTo(map);
				marker.bindPopup("<b>" + bikestation.stationName
						+ "</b><br>" + bikestation.totalDocks
						+ " docks (total)<br>" + bikestation.availableDocks
						+ " available");
				markers[location] = marker;
			}
		}
	};
	

	
});
