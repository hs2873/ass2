<!DOCTYPE html>
<html>
  <head>
    <title>Markers</title>
      <link href="bootstrap\css\bootstrap.min.css" rel="stylesheet">
    <style>
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map {
        height: 100%;
        width: 75%;
        float: left;
      }
      #trend {
      	height: 100%;
        width: 25%;
        float: right;
      }
#floating-panel {
  position: absolute;
  top: 10px;
  left: 25%;
  z-index: 5;
  background-color: #fff;
  padding: 5px;
  border: 1px solid #999;
  text-align: center;
  font-family: 'Roboto','sans-serif';
  line-height: 30px;
  padding-left: 10px;
}

    </style>
  </head>
  <body>
    
	<div id="trend">
		<p align="center">
          <input type = "text"
                 id = "place"/>
          <button type="button" class ="btn btn-large btn-success" onclick="getTrends();" >Go</button>
         
         
        </p>
        <p align="center"> 
          <label>Trending topics</label>
          <textarea id = "topics"
                  rows = "20"
                  style="width:90%;height:90%" ></textarea>
        </p>
	</div>
	
	<div id="map"></div>
	
	
    
    
    <script>


var wsServlet;
var wsServer;
var map;
var markers = [];

function initMap() {
	
	<% 
	
	//new Worker().start();
	%>
  var atlantic = {lat: 20, lng: -30};

  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 3,
    center: atlantic,
    
  });

  openSocket();
}

// Adds a marker to the map and push to the array.
function addMarker(location,color,animate) {
  var marker;
  if (animate) {
	  marker = new google.maps.Marker({
		    position: location,
		    map: map,
		    animation: google.maps.Animation.BOUNCE,
	  }); 
	  setTimeout(function(){ marker.setAnimation(null); }, 750);
  }
  else {
	  marker = new google.maps.Marker({
		    position: location,
		    map: map,
	  });  
  }
  
  if (color == "green"){
  	marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
  }
  else if (color == "red"){
  	marker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
  }
  else {
	marker.setIcon('http://maps.google.com/mapfiles/ms/icons/yellow-dot.png');  
  }
  markers.push(marker);
}


function getTrends(){
	var text = document.getElementById("place").value;
	//map.panTo(text);
	
//	var geocoder = new GClientGeocoder();
	var geocoder = new google.maps.Geocoder();
	var result = "";
	var lat,lng;
	geocoder.geocode( { 'address': text}, function(results, status) {
	     if (status == google.maps.GeocoderStatus.OK) {
	        lat = results[0].geometry.location.lat();
	         lng = results[0].geometry.location.lng();
	         console.log(lat+","+lng);
	         var newLoc = new google.maps.LatLng( lat,lng);  	
	         map.panTo(newLoc);
	         map.setZoom(5);
	     } else {
	         result = "Unable to find address: " + status;
	     }
	    	
	    });

	
	//  var latitude=place.geometry.location[text];
	//  var longitude=place.geometry.location[text];
  //    var newLoc = new google.maps.LatLng(  lat,lng);  	

	  
	
    wsServer.send(text);
}

function openSocket(){
    // Create a new instance of the websocket
// wsServlet = new WebSocket("ws://localhost:9080/snstry/Servlet");
// wsServer = new WebSocket("ws://localhost:9080/snstry/Server");
    
 wsServlet = new WebSocket("ws://54.86.201.62:8080/Servlet");
 wsServer = new WebSocket("ws://54.86.201.62:8080/Server");
   //wsServlet = new WebSocket("ws://52.91.184.225:8080/Servlet");
  // wsServer = new WebSocket("ws://52.91.184.225:8080/Server");
  
    wsServlet.onopen = function(event){
        if(event.data === undefined)
            return;
        //writeResponse(event.data);
    };
    wsServer.onopen = function(event){
        if(event.data === undefined)
            return;
        //writeResponse(event.data);
    };

    wsServlet.onmessage = function(event){
    	var x = event.data.split(",");
    	var newLoc = new google.maps.LatLng(parseFloat(x[0]),parseFloat(x[1]));  	
    	var score = x[2];
		if (score < 0.0){
			addMarker(newLoc,"red",true);
		}
		else if (score > 0.0){
			addMarker(newLoc,"green",true);
		}
    	else {
    		addMarker(newLoc,"yellow",true);
    	}  
		
    	//map.setCenter(newLoc);
        map.panTo(newLoc);
    };
    
    wsServer.onmessage = function(event){
    	if (event.data.split("\n").length > 1) {
    		document.getElementById("topics").value = event.data;
    	}
    	else {
 
    		var x = event.data.split(",");
    		var newLoc = new google.maps.LatLng(parseFloat(x[0]),parseFloat(x[1]));  	
    		var score = x[2];
    		if (score < 0.0){
    			addMarker(newLoc,"red",false);
    		}
    		else if (score > 0.0){
    			addMarker(newLoc,"green",false);
    		}
        	else {
        		addMarker(newLoc,"yellow",false);
        	} 
    		 
    	}
    };

    wsServlet.onclose = function(event){       
    };
    wsServer.onclose = function(event){        
    };
   
}



    </script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDJExaHJk9XpC28JZwVhhMzW-UsKDt86HQ&signed_in=true&callback=initMap"></script>
  </body>
</html>