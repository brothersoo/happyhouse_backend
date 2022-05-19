package com.ssafy.happyhouse.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ssafy.happyhouse.model.house.DealInfo;

public interface HouseService {

	List<DealInfo> getDealsByCodeDate(String code, int dealYear, int dealMonth, String standard);
	
	int updateDeal(String code, int year, int month) throws IOException, ParserConfigurationException, SAXException;
}
