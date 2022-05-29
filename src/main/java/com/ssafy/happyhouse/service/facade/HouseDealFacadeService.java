package com.ssafy.happyhouse.service.facade;

import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface HouseDealFacadeService {

  int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException;

  ChartData getChartDataOfHouses(List<Long> houseIds, DateRange dateRange);
}
