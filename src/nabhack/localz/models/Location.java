package nabhack.localz.models;

import com.google.gson.annotations.Expose;

public class Location {

	@Expose
	private float lat;
	@Expose
	private float lng;
	
	public Location(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
}
