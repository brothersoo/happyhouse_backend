package com.ssafy.happyhouse.model.area;

public class Upmyundong {

	private String name;
	private String code;
	private String lat;
	private String lng;
	private Sigugun sigugun;

	public Upmyundong() {}

	public Upmyundong(String name, String code, String lat, String lng, Sigugun sigugun) {
		super();
		this.name = name;
		this.code = code;
		this.lat = lat;
		this.lng = lng;
		this.sigugun = sigugun;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public Sigugun getSigugun() {
		return sigugun;
	}

	public void setSigugun(Sigugun sigugun) {
		this.sigugun = sigugun;
	};
	
	public String toJson() {
		return new StringBuilder()
				.append("{\"code\": \"").append(code).append("\",")
				.append("\"name\": \"").append(name).append("\"}").toString();
	}
}
