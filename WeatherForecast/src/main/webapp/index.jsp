<%@ page import="fmiprojects.weather.*" %>
<%@ page import="fmiprojects.weather.util.*" %>
<%@ page import="com.cdyne.ws.weatherws.*" %>
<%@ page import="java.util.*" %>
<%
ZipCodeClient zipCodeClient = new ZipCodeClient(Config.get("zipcodeurl"));
String cityParam = request.getParameter("city");
Weather weather = new Weather();

WeatherReturn weatherReturn = null;
ForecastReturn forecastReturn = null;
City city = null;

List<City> cities = zipCodeClient.get(cityParam, 5);
if (cities.size() > 0) {
	city = cities.get(0);
	weatherReturn = weather.getWeatherSoap().getCityWeatherByZIP(city.zip);
	forecastReturn = weather.getWeatherSoap().getCityForecastByZIP(city.zip);
}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
  <title>Weather Forecast</title>
  <link rel="stylesheet" href="stylesheets/base.css" type="text/css" media="screen" />
  <link rel="stylesheet" href="stylesheets/themes/default/style.css" type="text/css" media="screen" />
  <link rel="stylesheet" href="stylesheets/override.css" type="text/css" media="screen" />
  <link rel="stylesheet" href="stylesheets/ui-lightness/jquery-ui-1.8.9.custom.css" type="text/css" media="screen" />
  <script type="text/javascript" src="javascripts/jquery-1.4.4.min.js"></script>
  <script type="text/javascript" src="javascripts/jquery-ui-1.8.9.custom.min.js"></script>
  <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>  
  <script type="text/javascript">
	$(function() {
		function log(message) {
			console.log(message);
		}
		$("#city")
			.click(function() {
				if (this.value == this.title) this.value = "";
			})
			.autocomplete({
				source: function(request, response) {
					$.getJSON("/weather/search", {city: request.term, limit: 10}, function(data) {
						response(data);
					});
				},
				minLength: 2
			});
	});
	</script> 
</head>
<body>
  <div id="container">
    <div id="header">
      <div class="centerer">
          <h1><a href="index.jsp">Weather Forecast</a></h1>
          <form action="index.jsp" method="get" class="form" id="search">
            <div class="group ui-widget">
              <input type="text" name="city" id="city" title="Enter city or zip code" value="Enter city or zip code" class="text_field" />
              <span class="description">Ex: New York or 10001</span>
            </div>
            <div class="group navform">
              <button class="button" type="submit">Go</button>
            </div>
          </form>
          <h3>
              <%= DateUtil.now("dd.MM.yyyy") %> <br /> <%= DateUtil.now("HH:mm") %>               
          </h3>
      </div>
    </div>
    <div id="wrapper" class="wat-cf">
      <div id="main">
      
      <% if (city == null) { %>
	  <div class="flash">
    		<div class="message notice">
    			<p>Enter city or zip code in search box above to view today's weather and 7 days forecast</p>
    		</div>
      </div>      
      <% } %>
      
      <% if (city != null && !weatherReturn.isSuccess()) { %>
	  <div class="flash">
    		<div class="message warning">
    			<p>City not found in the weather database</p>
    		</div>
      </div>      
      <% } %>      

	  <% if (city != null && weatherReturn.isSuccess()) { %>
        <div class="block">
          <div class="content">
            <h2 class="title"><%= city.city %>, <%= city.state %> <span class="description">zip: <%= city.zip %>, time zone: <%= city.timezone %>, longitude: <%= city.longitude %>, latitude: <%= city.latitude %></span></h2>

            <div class="inner">
                <div class="today">
                    <h4>The weather today <%= DateUtil.now("dd.MM.yyyy") %>, at <%= DateUtil.now("HH:mm") %></h4>
                    
                    <div class="picture">
                       <img src="<%= WeatherUtil.getPictureUrl(weather, weatherReturn.getWeatherID()) %>" />
                    </div>                    
                    
                    <ul>
                    	<li>Description: <%= weatherReturn.getDescription() %></li>
                    	<li>Pressure: <%= weatherReturn.getPressure() %></li>
                    	<li>Relative Humidity: <%= weatherReturn.getRelativeHumidity() %></li>
                    	<li>Remarks: <%= weatherReturn.getRemarks() %></li>
                    	<li>Temperature: <%= weatherReturn.getTemperature() %></li>
                    	<li>Visibility: <%= weatherReturn.getVisibility() %></li>
                    	<li>Station: <%= weatherReturn.getWeatherStationCity() %></li>
                    	<li>Wind: <%= weatherReturn.getWind() %></li>
                    	<li>Wind Chill: <%= weatherReturn.getWindChill() %></li>
                    </ul>        
                    
                    <br />
                    
                    <div id="map_canvas" style="width: 350px; height: 350px"></div>
					<script type="text/javascript">
					  (function() {
					    var latlng = new google.maps.LatLng(<%= city.latitude %>, <%= city.longitude %>);
					    var myOptions = {
					      zoom: 9,
					      center: latlng,
					      mapTypeId: google.maps.MapTypeId.ROADMAP
					    };
					    var map = new google.maps.Map(document.getElementById("map_canvas"),
					        myOptions);
					  })();					
					</script>                                                                       
                </div> 
                <div class="forecast">
                    <h4>7 days forecast</h4>

                    <ul>
                    <% for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) { 
                    %>
                    <li>                    
                      <div class="top">
                        <img src="<%= WeatherUtil.getPictureUrl(weather, forecast.getWeatherID()) %>" />
                      </div>
                      <div class="item">
                        <ul>
                        	<li>Date: <%= DateUtil.format(forecast.getDate().toGregorianCalendar().getTime(), "dd.MM.yyyy")  %></li>
                        	<li>Description: <%= forecast.getDesciption() %></li>
                        	<li>Temperatures: <%= forecast.getTemperatures().getMorningLow() %> - <%= forecast.getTemperatures().getDaytimeHigh() %></li>
                        	<li>POP: <%= forecast.getProbabilityOfPrecipiation().getDaytime() %> - <%= forecast.getProbabilityOfPrecipiation().getNighttime() %> </li>                        	
                        </ul>
                      </div>
                    </li>
                    <% } %>                   
                  </ul>                    
                </div>
            </div>
          </div>          
        </div>  
        <% } %>      

        <div id="footer">
          <div class="block">
            <p>Copyright &copy; 2011 Weather Forecast.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>