package fmiprojects.weather;

import com.cdyne.ws.weatherws.Weather;

public class WeatherTest {
    public static void main(String[] args) {
		Weather weather = new Weather();
		System.out.println(weather.getWeatherSoap().getWeatherInformation().getWeatherDescription());
		System.out.println(weather.getWeatherSoap().getCityForecastByZIP("10001"));
		System.out.println(weather.getWeatherSoap().getCityWeatherByZIP("10001"));
	}
}