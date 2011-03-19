package fmiprojects.weather;

public class City {
	public City(String city, String state, String zip, String dst,
			String timezone, String longitude, String latitude) {
		super();
		this.state = state;
		this.city = city;
		this.zip = zip;
		this.dst = dst;
		this.timezone = timezone;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public final String state;
	public final String city;
	public final String zip;
	public final String dst;
	public final String timezone;
	
	public final String longitude;
	public final String latitude;	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result
				+ ((timezone == null) ? 0 : timezone.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		City other = (City) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (timezone == null) {
			if (other.timezone != null)
				return false;
		} else if (!timezone.equals(other.timezone))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "City [state=" + state + ", city=" + city + ", zip=" + zip
				+ ", dst=" + dst + ", timezone=" + timezone + ", longitude="
				+ longitude + ", latitude=" + latitude + "]";
	}
}
