package fmiprojects.weather.util;

import com.cdyne.ws.weatherws.*;
import java.util.*;

public class WeatherUtil {
	public static String getPictureUrl(Weather weather, short weatherId) {
		List<WeatherDescription> weatherDescriptions = weather.getWeatherSoap().getWeatherInformation().getWeatherDescription();
		
		for (WeatherDescription weatherDescription : weatherDescriptions) {
			if (weatherDescription.getWeatherID() == Math.abs(weatherId)) {
				return weatherDescription.getPictureURL();
			}
		}
		return "";
	}
	
	public static boolean isValidZipCode(String cityZipCode){
		if(cityZipCode == null){
			return false;
		}
		boolean retval = false;
	    String zipCodePattern = "\\d{5}(-\\d{4})?";
	    retval = cityZipCode.matches(zipCodePattern);
	    return retval;
	}
}
