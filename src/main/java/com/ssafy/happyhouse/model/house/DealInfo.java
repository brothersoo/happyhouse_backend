package com.ssafy.happyhouse.model.house;

import java.util.Objects;

public class DealInfo {
	private int id;
	private String type;
	private int floor;
	private int price;
	private float exclusivePrivateArea;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private HouseInfo houseInfo;
	
	public DealInfo() {};
	
	public DealInfo(int id, String type, int floor, int price, float exclusivePrivateArea,
			int dealYear, int dealMonth, int dealDay, HouseInfo houseInfo) {
		super();
		this.id = id;
		this.type = type;
		this.floor = floor;
		this.price = price;
		this.exclusivePrivateArea = exclusivePrivateArea;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
		this.houseInfo = houseInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public float getExclusivePrivateArea() {
		return exclusivePrivateArea;
	}

	public void setExclusivePrivateArea(float exclusivePrivateArea) {
		this.exclusivePrivateArea = exclusivePrivateArea;
	}

	public int getDealYear() {
		return dealYear;
	}

	public void setDealYear(int dealYear) {
		this.dealYear = dealYear;
	}

	public int getDealMonth() {
		return dealMonth;
	}

	public void setDealMonth(int dealMonth) {
		this.dealMonth = dealMonth;
	}
	
	public int getDealDay() {
		return dealDay;
	}

	public void setDealDay(int dealDay) {
		this.dealDay = dealDay;
	}

	public HouseInfo getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(HouseInfo houseInfo) {
		this.houseInfo = houseInfo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dealDay, dealMonth, dealYear, floor, houseInfo, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DealInfo other = (DealInfo) obj;
		return dealDay == other.dealDay && dealMonth == other.dealMonth && dealYear == other.dealYear
				&& floor == other.floor && Objects.equals(houseInfo, other.houseInfo)
				&& Objects.equals(type, other.type);
	}
}
