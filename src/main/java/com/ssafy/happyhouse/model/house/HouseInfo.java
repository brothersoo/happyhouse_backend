package com.ssafy.happyhouse.model.house;

import java.util.Objects;

import com.ssafy.happyhouse.model.area.Upmyundong;

public class HouseInfo {

	private String aptName;
	private int buildYear;
	private String jibun;
	private Upmyundong upmyundong;

	public HouseInfo() {};

	public HouseInfo(String aptName, int buildYear, String jibun, Upmyundong upmyundong) {
		this.aptName = aptName;
		this.buildYear = buildYear;
		this.jibun = jibun;
		this.upmyundong = upmyundong;
	}

	public String getAptName() {
		return aptName;
	}
	public void setAptName(String aptName) {
		this.aptName = aptName;
	}
	public int getBuildYear() {
		return buildYear;
	}
	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}
	public String getJibun() {
		return jibun;
	}
	public void setJibun(String jibun) {
		this.jibun = jibun;
	}
	public Upmyundong getUpmyundong() {
		return upmyundong;
	}
	public void setUpmyundong(Upmyundong upmyundong) {
		this.upmyundong = upmyundong;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aptName, upmyundong.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HouseInfo other = (HouseInfo) obj;
		return Objects.equals(aptName, other.aptName)
				&& Objects.equals(upmyundong.getName(), other.upmyundong.getName());
	}
}
