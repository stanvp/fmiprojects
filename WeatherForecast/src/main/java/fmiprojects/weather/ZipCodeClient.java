package fmiprojects.weather;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fmiprojects.weather.util.DocumentUtil;

public class ZipCodeClient {
	private String baseurl;
	
	public ZipCodeClient(String baseurl) {
		this.baseurl = baseurl;
	}
	
	private List<City> request(String url) throws IOException {
		List<City> cities = new ArrayList<City>();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		HttpEntity entity = response.getEntity();
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();		
		
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    Document doc = DocumentUtil.getDocument(instream);
		     
		    NodeList nodes;
			try {
				nodes = (NodeList) xpath.compile("/cities/city").evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		    
		    for (int i = 0; i < nodes.getLength(); i++) {
		    	Element city = (Element) nodes.item(i);
		    	
		    	cities.add(new City(
		    		city.getElementsByTagName("city").item(0).getTextContent(),
		    		city.getElementsByTagName("state").item(0).getTextContent(),
		    		city.getElementsByTagName("zip").item(0).getTextContent(),
		    		city.getElementsByTagName("dst").item(0).getTextContent(),
		    		city.getElementsByTagName("timezone").item(0).getTextContent(),
		    		city.getElementsByTagName("longitude").item(0).getTextContent(),
		    		city.getElementsByTagName("latitude").item(0).getTextContent()
		    	));
		    }
		}
		
		return cities;
	}
	
	public List<City> get(String city, int limit) throws IOException {
		if(city == null){
			return new  ArrayList<City>();
		}
		return request(baseurl + String.format("/%s/" + limit, URLEncoder.encode(city, "utf-8")));
	}
	
	public List<City> get(String city, int limit, String group) throws IOException {
		if (city == null) {
			return new ArrayList<City>();
		}
		return request(baseurl + String.format("/%s/%s/%s", URLEncoder.encode(city, "utf-8"), limit, URLEncoder.encode(group, "utf-8")));
	}
}