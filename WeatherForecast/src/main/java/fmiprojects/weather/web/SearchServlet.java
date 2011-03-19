package fmiprojects.weather.web;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.*;                                                         
import javax.servlet.http.*;

import com.google.gson.Gson;

import fmiprojects.weather.City;
import fmiprojects.weather.Config;
import fmiprojects.weather.ZipCodeClient;

public class SearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
    
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json");
    	
    	int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
    	
    	ZipCodeClient zipCodeClient = new ZipCodeClient(Config.get("zipcodeurl"));
    	List<City> cities = zipCodeClient.get(request.getParameter("city") + "%", 10, "city");
    	Set<Item> items = new LinkedHashSet<Item>();
    	for (City city : cities) {
    		items.add(new Item(city.city, city.city));
    		if (items.size() > limit) break;
    	}
    	
    	Gson gson = new Gson();
    	
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(items));
        out.flush();
        out.close();
    }
}

class Item {
	public final String label;
	public final String value;
	public Item(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
