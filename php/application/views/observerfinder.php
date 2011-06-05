 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAbqzrqBW5qKMmKBwX6OcvPRSU1oNbLog9qgW2L1te013aIJp1cBTh0oohL6569xKhiTNdC3wfevJO8w" type="text/javascript"></script>

<script type="text/javascript">
	function directions(){  
	  var start = new google.maps.LatLng(47.64628,-122.1377);
	  var end = new google.maps.LatLng(47.65213,-122.13018); 
	  var request = {
	    origin:start,
    	destination:end
  	  };
	  google.maps.directionsService.route(request, function(result, status) {
	    if (status == google.maps.DirectionsStatus.OK) {
			console.log(result);
		}
	 });
	}
</script>

<body onload="directions()"></body>

