package com.ssafy.happyhouse.util.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ssafy.happyhouse.model.house.HouseDeal;

public class HouseDealAPIHandler {
	
	private static final String serviceKey = "DeOFNi7MWb1qt2ViSK2nNbFU7E9JGmDyHV%2Fu6%2F8AAibluqWZYJbPykptZ5cRcKhLAYpQAvAAlfFjAc1NkCcahA%3D%3D";
	private static final String serviceUrl = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade";
	
	
	private static HouseDealAPIHandler houseDealAPIHandler = new HouseDealAPIHandler();
	
	public static HouseDealAPIHandler getInstance() {
		return houseDealAPIHandler;
	}
	
	public List<HouseDeal> getMonthlyAreaHouseDeal(String code, int dealYear, int dealMonth)
			throws IOException, ParserConfigurationException, SAXException {
		String dealDate;
		if (dealMonth < 10) {
			dealDate = dealYear + "0" + dealMonth; 
		} else {
			dealDate = dealYear + "" + dealMonth;
		}

		String paramUrl = new StringBuilder(serviceUrl)
				.append('?').append(URLEncoder.encode("serviceKey", "UTF-8")).append('=').append(serviceKey)
				.append('&').append(URLEncoder.encode("LAWD_CD", "UTF-8")).append('=').append(URLEncoder.encode(code, "UTF-8"))
				.append('&').append(URLEncoder.encode("DEAL_YMD", "UTF-8")).append('=').append(URLEncoder.encode(dealDate, "UTF-8"))
				.toString();
		
		URL url = new URL(paramUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		
		InputStream in = conn.getInputStream();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		HouseDealSaxHandler handler = new HouseDealSaxHandler();
		
		InputSource inputSource = new InputSource(in);
		inputSource.setEncoding("UTF-8");
		saxParser.parse(inputSource, handler);
		
		return handler.getHouseDeals();
	}
}
