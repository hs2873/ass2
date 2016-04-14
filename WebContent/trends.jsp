<!doctype html>
<html lang="en" class="no-js">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700|Droid+Serif' rel='stylesheet' type='text/css'>

	<link rel="stylesheet" href="design/css/reset.css"> <!-- CSS reset -->
	<link rel="stylesheet" href="design/css/style.css"> <!-- Resource style -->
	<link href="bootstrap\css\bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="navbar.css" rel="stylesheet">
	<script src="design/js/modernizr.js"></script> <!-- Modernizr -->
  	
	<title>Slide In Panel | CodyHouse</title>
</head>

<body>
   <div class="container">

      <!-- Static navbar -->
      <nav class="navbar navbar-default">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
             <a class="btn btn-primary btn-lg" href="markers.jsp">TweetGet</a>
           
          </div>
          <div id="navbar" class="navbar-collapse collapse">
                        
            
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </nav>

      <!-- Main component for a primary marketing message or call to action -->
      
   

    </div> <!-- /container -->
<div >
		<img src="images/giphy.gif" style="width: 488px;height: 256px;" ><img src="images/hand.gif" style="height: 260px; width: 427px"><img src="images/face.gif" style="width: 428px; height: 258px"> 
	
	

	 
	 </div>

 
 
	<main class="cd-main-content">
		<h1><b>Trumps everyone's imagination</b> </h1>
		<a href="#0" class="cd-btn">Fire Statistics</a>
		<!-- your content here -->
	</main>
	 

	<div class="cd-panel from-right">
		<header class="cd-panel-header">
			<h1>Change We Need</h1>
			<a href="#0" class="cd-panel-close">Close</a>
		</header>

		<div class="cd-panel-container">
			<div class="cd-panel-content">
			<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart']}]}"></script>
       <div id="piechart" style="width: 900px; height: 500px;"></div>
  
  <script>
  google.load("visualization", "1", {packages:["corechart"]});
         google.setOnLoadCallback(drawChart);
     var array;
     var chart;
     var data;    
     var options;	
         
         function drawChart() {

         data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Twitter',      1],
          ['Instagram',  1],
          ['Foursquare', 1],
          ['Path',    1],
          ['TweetMyJOBS',1],
          ['Other',1]
        ]);

        
        array=['Task','Twitter','Instagram','Foursquare','Path','Other'];
         options = {      };
		
        chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
      }
			</script>
						</div> <!-- cd-panel-content -->
		</div> <!-- cd-panel-container -->
	</div> <!-- cd-panel -->
<script src="design/js/jquery-2.1.1.js"></script>
<script src="design/js/main.js"></script> <!-- Resource jQuery -->
</body>
</html>