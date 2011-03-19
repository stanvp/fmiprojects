package fmiprojects.weather;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ZipCodeClientTest {
	ZipCodeClient client;
	
	@Before
	public void setUp() {
		client = new ZipCodeClient("http://localhost:4567/zipcodes");
	}

	@Test
	public void testGetString() throws IOException {
		List<City> cities = client.get("Portsmouth", 10);
		City expected = new City("Portsmouth", "NH", "00210", "1", "-5", "-71.013202", "43.005895");
		assertEquals(expected, cities.get(0));
	}

	@Test
	public void testGetStringString() throws IOException {
		List<City> cities = client.get("Portsmouth", 10);
		City expected = new City("Portsmouth", "NH", "00210", "1", "-5", "-71.013202", "43.005895");
		assertEquals(expected, cities.get(0));
	}
}