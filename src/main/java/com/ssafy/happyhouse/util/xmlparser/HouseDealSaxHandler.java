package com.ssafy.happyhouse.util.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.ssafy.happyhouse.model.area.Upmyundong;
import com.ssafy.happyhouse.model.house.DealInfo;
import com.ssafy.happyhouse.model.house.HouseDeal;
import com.ssafy.happyhouse.model.house.HouseInfo;

public class HouseDealSaxHandler extends DefaultHandler {

	  private List<HouseDeal> houseDeals;
	  private HouseDeal houseDeal;
	  private String str;

	  public HouseDealSaxHandler() {
	    houseDeals = new ArrayList<>();
	  }

	  public void startElement(String uri, String localName, String name, Attributes att) {
		  if (name.equals("item")) {
			  Upmyundong umd = new Upmyundong(); 
			  HouseInfo houseInfo = new HouseInfo();
			  houseInfo.setUpmyundong(umd);
			  DealInfo dealInfo = new DealInfo();
			  dealInfo.setHouseInfo(houseInfo);
			  houseDeal = new HouseDeal(houseInfo, dealInfo);
			  houseDeals.add(houseDeal);
	    }
	  }

	  public void endElement(String uri, String localName, String name) {
	    if (name.equals("거래금액")) {
	      str = str.trim().replace(",", "");
	      houseDeal.getDealInfo().setPrice(Integer.parseInt(str));
	    } else if (name.equals("거래유형")) {
	      houseDeal.getDealInfo().setType(str);
	    } else if (name.equals("건축년도")) {
	      houseDeal.getHouseInfo().setBuildYear(Integer.parseInt(str));
	    } else if (name.equals("년")) {
	      houseDeal.getDealInfo().setDealYear(Integer.parseInt(str));
	    } else if (name.equals("법정동")) {
	      houseDeal.getHouseInfo().getUpmyundong().setName(str.trim());
	    } else if (name.equals("아파트")) {
	      houseDeal.getHouseInfo().setAptName(str);
	    } else if (name.equals("월")) {
	      houseDeal.getDealInfo().setDealMonth(Integer.parseInt(str));
	    } else if (name.equals("일")) {
	      houseDeal.getDealInfo().setDealDay(Integer.parseInt(str));
	    } else if (name.equals("전용면적")) {
	      houseDeal.getDealInfo().setExclusivePrivateArea(Float.parseFloat(str));
	    } else if (name.equals("지번")) {
	      houseDeal.getHouseInfo().setJibun(str);
	    } else if (name.equals("지역코드")) {
	      houseDeal.getHouseInfo().getUpmyundong().setCode(str);
	    } else if (name.equals("층")) {
	      houseDeal.getDealInfo().setFloor(Integer.parseInt(str));
	    }
	  }

	  @Override
	  public void characters(char[] ch, int start, int length) {
	    str = new String(ch, start, length);
	  }

	  public List<HouseDeal> getHouseDeals() {
	    return houseDeals;
	  }

	  public void setHouseDeals(List<HouseDeal> houseDeals) {
	    this.houseDeals = houseDeals;
	  }
	}
