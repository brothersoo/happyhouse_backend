package com.ssafy.happyhouse.util.housedeal;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.helpers.DefaultHandler;

public class HouseDealSaxHandler extends DefaultHandler {
	private List<DealInfo> dealInfos;
	private int price;
	private String type;
	private int buildYear;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private String aptName;
	private float exclusivePrivateArea;
	private String jibun;
	private int floor;
	private String str;
	private Long umdId;

	public HouseDealSaxHandler(Long umdId) {
		dealInfos = new ArrayList<>();
		this.umdId = umdId;
	}

//	public void startElement(String uri, String localName, String name, Attributes att) {
//		if (name.equals("item")) {
//			Upmyundong umd = Upmyundong.builder().build();
//			HouseInfo houseInfo = HouseInfo.builder().upmyundong(umd).build();
//			DealInfo dealInfo = DealInfo.builder().houseInfo(houseInfo).build();
//			dealInfos.add(dealInfo);
//		}
//	}

	public void endElement(String uri, String localName, String name) {
		if (name.equals("거래금액")) {
			str = str.trim().replace(",", "");
			this.price = Integer.parseInt(str);
		} else if (name.equals("거래유형")) {
			this.type = str;
		} else if (name.equals("건축년도")) {
			this.buildYear = Integer.parseInt(str);
		} else if (name.equals("년")) {
			this.dealYear = Integer.parseInt(str);
		} else if (name.equals("아파트")) {
			this.aptName = str;
		} else if (name.equals("월")) {
			this.dealMonth = Integer.parseInt(str);
		} else if (name.equals("일")) {
			this.dealDay = Integer.parseInt(str);
		} else if (name.equals("전용면적")) {
			this.exclusivePrivateArea = (float)(Math.round(Float.parseFloat(str)*1000)/1000.0);
		} else if (name.equals("지번")) {
			this.jibun = str;
		} else if (name.equals("층")) {
			this.floor = Integer.parseInt(str);
		} else if (name.equals("item")) {
			Upmyundong umd = Upmyundong.builder().id(umdId).build();
			HouseInfo houseInfo = HouseInfo.builder().aptName(aptName)
					.jibun(jibun).buildYear(buildYear).upmyundong(umd).build();
			DealInfo dealInfo = DealInfo.builder()
					.dealDate(LocalDate.of(dealYear, dealMonth, dealDay))
					.type(type).exclusivePrivateArea(exclusivePrivateArea)
					.price(price).floor(floor).houseInfo(houseInfo).build();
			dealInfos.add(dealInfo);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		str = new String(ch, start, length);
	}

	public List<DealInfo> getDealInfos() {
		return dealInfos;
	}

	public void setDealInfos(List<DealInfo> dealInfos) {
		this.dealInfos = dealInfos;
	}
}