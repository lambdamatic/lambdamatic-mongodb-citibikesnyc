var map = L.map('map')
// this 'moveend' event is also triggered when the map is initialized
map.on('moveend', onMapMoved); 
// starting in central park: 
map.setView([40.782833,-73.965033], 15);

L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
	maxZoom: 15,
	minZoom: 12,
	attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
		'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
		'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'xcoulon.jnlla4f1',
}).addTo(map);

function onMapMoved() {
    getBikeStations();
}

function getBikeStations() {
	var southWest=toString(map.getBounds().getSouthWest());
	var northWest=toString(map.getBounds().getNorthWest());
	var northEast=toString(map.getBounds().getNorthEast());
	var southEast=toString(map.getBounds().getSouthEast());
	$.get(document.location + "rest/bikestations?location=" + southWest + "&location=" + northWest + "&location=" + northEast + "&location=" + southEast, 
			function result(data){
				console.log("Bikestation within bounds:" + southWest + " / " + northWest + " / " + northEast + " / " + southEast);
	});
}

function toString(location) {
	return "" + location.lat + "," + location.lng;
}