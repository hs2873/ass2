<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Hello World Comet Application</title>
<script src="streamhub-min.js" type="text/javascript"></script>
</head>
<body>
<h1>Hello World Comet Application</h1>
<input type="button" value="Start streaming" onclick="startStreaming()">
<div id="streamingData"></div>
<script>
function topicUpdated(sTopic, oData) {
var newDiv = document.createElement("DIV");
newDiv.innerHTML = "Update for topic '" + sTopic + "' Response: '" + oData.Response + "'";
document.getElementById('streamingData').appendChild(newDiv);
}

function startStreaming() {
var hub = new StreamHub();
hub.connect("http://localhost:9080/");
hub.subscribe("HelloWorld", topicUpdated);
}
</script>
</body>
</html>