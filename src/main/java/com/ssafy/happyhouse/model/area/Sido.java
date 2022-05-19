package com.ssafy.happyhouse.model.area;

import java.util.Objects;

public class Sido {

	private int id;
	private String name;
	
	public Sido() {};
	
	public Sido(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sido other = (Sido) obj;
		return id == other.id;
	}

	public String toJson() {
		return new StringBuilder()
				.append("{\"id\": \"").append(id).append("\",")
				.append("\"name\": \"").append(name).append("\"}").toString();
	}
}

