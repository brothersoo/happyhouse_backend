package com.ssafy.happyhouse.model.area;

import java.util.Objects;

public class Sigugun {

	private int id;
	private String name;
	private Sido sido;
	
	public Sigugun() {}
	
	public Sigugun(int id, String name, Sido sido) {
		this.id = id;
		this.name = name;
		this.sido = sido;
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

	public Sido getSido() {
		return sido;
	}

	public void setSido(Sido sido) {
		this.sido = sido;
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
		Sigugun other = (Sigugun) obj;
		return id == other.id;
	}
	
	public String toJson() {
		return new StringBuilder()
				.append("{\"id\": \"").append(id).append("\",")
				.append("\"name\": \"").append(name).append("\"}").toString();
	}
}
