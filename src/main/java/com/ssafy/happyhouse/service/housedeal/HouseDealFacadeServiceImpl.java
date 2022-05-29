package com.ssafy.happyhouse.service.housedeal;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.dto.response.graph.Dataset;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import com.ssafy.happyhouse.repository.house.HouseRepository;
import com.ssafy.happyhouse.service.area.AreaFacadeService;
import com.ssafy.happyhouse.util.housedeal.HouseDealAPIHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

@RequiredArgsConstructor
@Service
public class HouseDealFacadeServiceImpl implements HouseDealFacadeService {

  private final HouseDealAPIHandler houseDealAPIHandler;
  private final HouseDealRepository houseDealRepository;
  private final HouseRepository houseRepository;
  private final AreaFacadeService areaFacadeService;

  @Override
  public List<HouseDeal> getDealsByCodeDate(String code, int dealYear, int dealMonth) {
    return houseDealRepository.findByCodeAndYearMonthOfDate(
        code, LocalDate.of(dealYear, dealMonth, 1));
  }

  @Override
  @Transactional
  public int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    List<HouseDeal> houseDealsFromOpenAPI = new ArrayList<>();

    // get persisted houses
    List<House> persistedHouses
        = houseRepository.findByCodeStartingWith(dealUpdateDto.getCode());
    Map<House, House> persistedHousesMap = new HashMap<>();
    for (House house : persistedHouses) {
      persistedHousesMap.put(house, house);
    }

    // get persisted house deals
    LocalDate fromDate = LocalDate.of(dealUpdateDto.getFromYear(), dealUpdateDto.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dealUpdateDto.getToYear(),
        dealUpdateDto.getToMonth(), 1).with(lastDayOfMonth());
    Set<HouseDeal> persistedHouseDeals = new HashSet<>(
        houseDealRepository.findByCodeAndDateBetween(
            dealUpdateDto.getCode(), fromDate, toDate));

    // api call in date range (monthly)
    for (int year = dealUpdateDto.getFromYear(); year <= dealUpdateDto.getToYear(); year++) {
      int fromMonth = (year == dealUpdateDto.getFromYear()) ? dealUpdateDto.getFromMonth() : 1;
      int toMonth = (year == dealUpdateDto.getToYear()) ? dealUpdateDto.getToMonth() : 12;
      for (int month = fromMonth; month <= toMonth; month++) {
        List<HouseDeal> dealsFromAPI = houseDealAPIHandler.getMonthlyAreaDealInfo(dealUpdateDto.getCode(), year, month);
        houseDealsFromOpenAPI.addAll(dealsFromAPI);
      }
    }

    // set upmyundong of house deals from external api
    Map<String, Upmyundong> upmyundongInAreaMap = new HashMap<>();
    List<Upmyundong> upmyundongsInArea = areaFacadeService.searchUpmyundongsInSigugun(
        dealUpdateDto.getCode());
    for (Upmyundong umd : upmyundongsInArea) {
      upmyundongInAreaMap.putIfAbsent(umd.getName(), umd);
    }
    for (HouseDeal houseDeal : houseDealsFromOpenAPI) {
      houseDeal.getHouse().setPersistedUpmyundong(
          upmyundongInAreaMap.get(houseDeal.getHouse().getUpmyundong().getName()));
    }

    List<HouseDeal> newHouseDeals = new ArrayList<>();
    Map<House, House> newHousesMap = new HashMap<>();

    for (HouseDeal houseDeal : houseDealsFromOpenAPI) {
      if (!persistedHousesMap.containsKey(houseDeal.getHouse())) {
        newHousesMap.putIfAbsent(houseDeal.getHouse(), houseDeal.getHouse());
        if (newHousesMap.containsKey(houseDeal.getHouse())) {
          houseDeal.setPersistedHouse(newHousesMap.get(houseDeal.getHouse()));
        }
      } else {
        houseDeal.setPersistedHouse(persistedHousesMap.get(houseDeal.getHouse()));
      }

      if (!persistedHouseDeals.contains(houseDeal)) {
        newHouseDeals.add(houseDeal);
      }
    }

    List<House> savedHouses = houseRepository.saveAll(newHousesMap.keySet());
    List<HouseDeal> savedHouseDeals = houseDealRepository.saveAll(newHouseDeals);

    return new int[]{savedHouses.size(), savedHouseDeals.size()};
  }

  Double getNearestData(Map<String, Double> dateAvgPriceMap, int startIndex, List<String> dates) {
    int gap = 0;
    while (true) {
      if (startIndex - gap >= 0 && dateAvgPriceMap.containsKey(dates.get(startIndex - gap))) {
        return dateAvgPriceMap.get(dates.get(startIndex - gap));
      } else if (startIndex + gap < dates.size() && dateAvgPriceMap.containsKey(dates.get(startIndex + gap))) {
        return dateAvgPriceMap.get(dates.get(startIndex + gap));
      } else {
        gap++;
      }
    }
  }

  Dataset generateDataset(Map<String, Map<String, Double>> houseDateAvgPriceMap,
      String houseName, List<String> dates) {
    Dataset dataset = Dataset.builder().label(houseName).data(new ArrayList<>()).build();
    for (int i = 0; i < dates.size(); i++) {
      Double nearestData = getNearestData(houseDateAvgPriceMap.get(houseName), i, dates);
      dataset.getData().add(nearestData);
    }
    return dataset;
  }

  ChartData generateChartData(List<AveragePricePerUnit> averagePricesPerUnit) {
    ChartData chartData = new ChartData();
    Map<String, Map<String, Double>> houseDateAvgPriceMap = new HashMap<>();
    Set<String> chartDataLabels = new HashSet<>();

    for (AveragePricePerUnit averagePrice : averagePricesPerUnit) {
      houseDateAvgPriceMap.putIfAbsent(averagePrice.getName(), new HashMap<>());
      houseDateAvgPriceMap.get(averagePrice.getName()).putIfAbsent(averagePrice.getDate(), averagePrice.getAvgPrice());
      chartDataLabels.add(averagePrice.getDate());
    }

    chartData.setLabels(new ArrayList<>(chartDataLabels));
    chartData.getLabels().sort(null);
    chartData.setDatasets(new ArrayList<>());

    for (String houseName : houseDateAvgPriceMap.keySet()) {
      chartData.getDatasets().add(
          generateDataset(houseDateAvgPriceMap, houseName, chartData.getLabels())
      );
    }

    return chartData;
  }

  @Override
  public ChartData getChartDataOfHouses(List<Long> houseIds, DateRange dateRange) {
    LocalDate fromDate = LocalDate.of(dateRange.getFromYear(),
        dateRange.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dateRange.getToYear(), dateRange.getToMonth(), 1)
        .with(lastDayOfMonth());

    List<AveragePricePerUnit> averagePricesPerUnit
        = houseDealRepository.findHouseAveragePriceByCodeAndDateRange(houseIds,
        fromDate, toDate, dateRange.getType());

    return generateChartData(averagePricesPerUnit);
  }

  @Override
  public List<HouseDeal> getDealOfApt(Long hosueId) {
    List<HouseDeal> dealList = houseDealRepository.findByHouseIdOrderByDealDateDesc(hosueId);
    return dealList;
  }

  @Override
  public List<House> getHousesInArea(String code) {
    return houseRepository.findByCodeStartingWith(code);
  }
}
