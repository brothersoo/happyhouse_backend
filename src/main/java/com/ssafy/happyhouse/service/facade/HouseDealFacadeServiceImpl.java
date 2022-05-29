package com.ssafy.happyhouse.service.facade;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HousesAndDeals;
import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.dto.response.graph.Dataset;
import com.ssafy.happyhouse.service.area.sigugun.SigugunService;
import com.ssafy.happyhouse.service.area.upmyundong.UpmyundongService;
import com.ssafy.happyhouse.service.house.HouseService;
import com.ssafy.happyhouse.service.housedeal.HouseDealService;
import com.ssafy.happyhouse.service.updateddealinfo.UpdatedDealInfoService;
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
  private final UpdatedDealInfoService updatedDealInfoService;
  private final SigugunService sigugunService;
  private final UpmyundongService upmyundongService;
  private final HouseService houseService;
  private final HouseDealService houseDealService;

  private HousesAndDeals getHouseDealsByCodeAndDateBetween(
      List<String> codes, LocalDate fromDate, LocalDate toDate,
      Map<String, Sigugun> codeSigugunMap, Set<UpdatedDealInfo> updatedDealInfos)
      throws IOException, ParserConfigurationException, SAXException {
    LocalDate now = LocalDate.now();
    List<UpdatedDealInfo> newUpdatedDealInfos = new ArrayList<>();

    HousesAndDeals res = new HousesAndDeals(new ArrayList<>(), new ArrayList<>());

    for (LocalDate date = fromDate; date.isBefore(toDate); date = date.plusMonths(1)) {
      for (String code : codes) {
        UpdatedDealInfo updatedDealInfo = UpdatedDealInfo.builder()
            .sigugun(codeSigugunMap.get(code)).date(date).build();
        if (!updatedDealInfos.contains(updatedDealInfo)) {
          HousesAndDeals housesAndDealsFromAPI = houseDealAPIHandler.getMonthlyAreaDealInfo(
              code, date.getYear(), date.getMonthValue());
          res.getHouses().addAll(housesAndDealsFromAPI.getHouses());
          res.getHouseDeals().addAll(housesAndDealsFromAPI.getHouseDeals());
          if (now.getYear() != date.getYear() || now.getMonth() != date.getMonth()) {
            newUpdatedDealInfos.add(updatedDealInfo);
          }
        }
      }
    }
    updatedDealInfoService.saveAll(newUpdatedDealInfos);
    return res;
  }

  @Override
  @Transactional
  public int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    LocalDate fromDate = LocalDate.of(dealUpdateDto.getFromYear(), dealUpdateDto.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dealUpdateDto.getToYear(),
        dealUpdateDto.getToMonth(), 1).with(lastDayOfMonth());

    // get persisted houses
    Set<House> persistedHousesSet = houseService.getHouseSetInSigugun(dealUpdateDto.getCodes());

    // get persisted house deals
    Set<HouseDeal> persistedHouseDeals = houseDealService.getHouseDealSetInSigugunBetweenDate(
        dealUpdateDto.getCodes(), fromDate, toDate);

    // get already updated area, date
    Set<UpdatedDealInfo> updatedDealInfos = updatedDealInfoService.findByCodeInAndDateBetweenSet(
        dealUpdateDto.getCodes(), fromDate, toDate);

    // get code-sigugun map
    Map<String, Sigugun> codeSigugunMap = sigugunService.getCodeSigugunMap(dealUpdateDto.getCodes());

    // get name-upmyudong map in request siguguns
    Map<String, Upmyundong> nameUpmyundongMap = upmyundongService.getNameUpmyundongMap(
        dealUpdateDto.getCodes());

    // api call in date range (monthly)
    HousesAndDeals housesAndDealsFromOpenAPI = getHouseDealsByCodeAndDateBetween(
        dealUpdateDto.getCodes(), fromDate, toDate, codeSigugunMap, updatedDealInfos
    );

    List<HouseDeal> newHouseDeals = new ArrayList<>();
    Set<House> newHousesSet = new HashSet<>();

    // set upmyundong of house deals from external api and persist
    for (House house : housesAndDealsFromOpenAPI.getHouses()) {
      house.setPersistedUpmyundong(nameUpmyundongMap.get(house.getUpmyundong().getName()));
      if (!persistedHousesSet.contains(house)) {
        newHousesSet.add(house);
      }
    }
    List<House> newHouses = new ArrayList<>(newHousesSet);
    int savedHousesNumber = houseService.batchInsert(newHouses);

    // set persisted house for house deals
    houseService.setPersistedHouse(newHouses, housesAndDealsFromOpenAPI);
    // get new house deals
    for (HouseDeal houseDeal : housesAndDealsFromOpenAPI.getHouseDeals()) {
      if (!persistedHouseDeals.contains(houseDeal)) {
        newHouseDeals.add(houseDeal);
      }
    }
    int savedHouseDealsNumber = houseDealService.batchInsert(newHouseDeals);

    return new int[]{savedHousesNumber, savedHouseDealsNumber};
  }

  private Double getNearestData(Map<String, Double> dateAvgPriceMap, int startIndex, List<String> dates) {
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

  @Transactional
  public int[] updateDealJpaVersion(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    LocalDate fromDate = LocalDate.of(dealUpdateDto.getFromYear(), dealUpdateDto.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dealUpdateDto.getToYear(),
        dealUpdateDto.getToMonth(), 1).with(lastDayOfMonth());


    // get persisted houses
    Map<House, House> persistedHousesMap
        = houseService.getHouseMapInSigugun(dealUpdateDto.getCodes());

    // get persisted house deals
    Set<HouseDeal> persistedHouseDeals = houseDealService.getHouseDealSetInSigugunBetweenDate(
        dealUpdateDto.getCodes(), fromDate, toDate);

    // get already updated area, date
    Set<UpdatedDealInfo> updatedDealInfos = updatedDealInfoService.findByCodeInAndDateBetweenSet(
        dealUpdateDto.getCodes(), fromDate, toDate);

    // get code-sigugun map
    Map<String, Sigugun> codeSigugunMap = sigugunService.getCodeSigugunMap(dealUpdateDto.getCodes());

    // get name-upmyudong map in request siguguns
    Map<String, Upmyundong> nameUpmyundongMap = upmyundongService.getNameUpmyundongMap(
        dealUpdateDto.getCodes());

    // api call in date range (monthly)
    HousesAndDeals houseDealsFromOpenAPI = getHouseDealsByCodeAndDateBetween(
        dealUpdateDto.getCodes(), fromDate, toDate, codeSigugunMap, updatedDealInfos
    );

    // set upmyundong of house deals from external api
    for (HouseDeal houseDeal : houseDealsFromOpenAPI.getHouseDeals()) {
      houseDeal.getHouse().setPersistedUpmyundong(
          nameUpmyundongMap.get(houseDeal.getHouse().getUpmyundong().getName()));
    }

    List<HouseDeal> newHouseDeals = new ArrayList<>();
    Map<House, House> newHousesMap = new HashMap<>();

    for (HouseDeal houseDeal : houseDealsFromOpenAPI.getHouseDeals()) {
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

    List<House> savedHouses = houseService.saveAll(newHousesMap.keySet());
    List<HouseDeal> savedHouseDeals = houseDealService.saveAll(newHouseDeals);

    return new int[]{savedHouses.size(), savedHouseDeals.size()};
  }

  private Dataset generateDataset(Map<String, Map<String, Double>> houseDateAvgPriceMap,
      String houseName, List<String> dates) {
    Dataset dataset = Dataset.builder().label(houseName).data(new ArrayList<>()).build();
    for (int i = 0; i < dates.size(); i++) {
      Double nearestData = getNearestData(houseDateAvgPriceMap.get(houseName), i, dates);
      dataset.getData().add(nearestData);
    }
    return dataset;
  }

  private ChartData generateChartData(List<AveragePricePerUnit> averagePricesPerUnit) {
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
        = houseDealService.findHouseAveragePriceByCodeAndDateRange(houseIds,
        fromDate, toDate, dateRange.getType());

    return generateChartData(averagePricesPerUnit);
  }
}
