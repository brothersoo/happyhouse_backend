package com.ssafy.happyhouse.service.housedeal;

import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;

import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AverageDealsInRange;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface HouseDealFacadeService {

  List<HouseDeal> getDealsByCodeDate(String code, int dealYear, int dealMonth);

  int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException;

  AverageDealsInRange getDealsByCodeAndDateRange(String code,
      Long houseId, DateRange dateRange);
  
  public List<HouseDeal> getDealOfApt(Long hosueId);

  List<House> getHousesInArea(String code);

}
