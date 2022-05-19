package com.ssafy.happyhouse.model.house;

public class HouseDeal {

	private HouseInfo houseInfo;
	private DealInfo dealInfo;
	
	public HouseDeal() {};
	
	public HouseDeal(HouseInfo houseInfo, DealInfo dealInfo) {
		this.houseInfo = houseInfo;
		this.dealInfo = dealInfo;
	}

	public HouseInfo getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(HouseInfo houseInfo) {
		this.houseInfo = houseInfo;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}
	
	public String toJSON() {
		return new StringBuilder()
				.append("{\"price\": ").append(dealInfo.getPrice()).append(",")
				.append("\"type\": \"").append(dealInfo.getType()).append("\",")
				.append("\"buildYear\": ").append(houseInfo.getBuildYear()).append(",")
				.append("\"dealYear\": ").append(dealInfo.getDealYear()).append(",")
				.append("\"upmyundong\": \"").append(houseInfo.getUpmyundong().getName()).append("\",")
				.append("\"aptName\": \"").append(houseInfo.getAptName()).append("\",")
				.append("\"dealMonth\": ").append(dealInfo.getDealMonth()).append(",")
				.append("\"dealDay\": ").append(dealInfo.getDealDay()).append(",")
				.append("\"exclusivePrivateArea\": ").append(dealInfo.getExclusivePrivateArea()).append(",")
				.append("\"jibun\": \"").append(houseInfo.getJibun()).append("\",")
				.append("\"floor\": ").append(dealInfo.getFloor()).append("}")
				.toString();
	}
}
