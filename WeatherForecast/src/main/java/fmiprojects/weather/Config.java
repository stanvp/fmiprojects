package fmiprojects.weather;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	public static String get(String name) {
		Properties prop = new Properties();
		 
    	try {
    		prop.load(Config.class.getResourceAsStream("/config.properties"));
    		return prop.getProperty(name);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
        }		
	}
}
