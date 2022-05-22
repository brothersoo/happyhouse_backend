package com.ssafy.happyhouse.service.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AverageDealsInRange;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface HouseDealFacadeService {

  List<DealInfo> getDealsByCodeDate(String code, int dealYear, int dealMonth);

  int[] updateDeal(String code, int year, int month, Long upmyundongId)
      throws IOException, ParserConfigurationException, SAXException;

  AverageDealsInRange getDealsByCodeAndDateRange(String code,
      Long houseId, DateRange dateRange);
}
