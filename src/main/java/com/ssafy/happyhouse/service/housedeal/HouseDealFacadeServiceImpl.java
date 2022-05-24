package com.ssafy.happyhouse.service.housedeal;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.AverageDealsInRange;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import com.ssafy.happyhouse.repository.housedeal.HouseRepository;
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

  private HouseDealAPIHandler houseDealAPIHandler = HouseDealAPIHandler.getInstance();
  private final HouseDealRepository houseDealRepository;
  private final HouseRepository houseRepository;

  @Override
  public List<HouseDeal> getDealsByCodeDate(String code, int dealYear, int dealMonth) {
    return houseDealRepository.findByCodeAndYearMonthOfDate(
        code, LocalDate.of(dealYear, dealMonth, 1));
  }

  @Override
  @Transactional
  public int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    List<HouseDeal> houseDealInfosFromOpenAPI = new ArrayList<>();

    // get persisted houses
    List<House> persistedHouses
        = houseRepository.findByUpmyundongId(dealUpdateDto.getUmdId());
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
        houseDealInfosFromOpenAPI.addAll(
            houseDealAPIHandler.getMonthlyAreaDealInfo(dealUpdateDto.getCode(), year, month,
                dealUpdateDto.getUmdId()));
      }
    }

    List<HouseDeal> newHouseDeals = new ArrayList<>();
    Map<House, House> newHouses = new HashMap<>();

    for (HouseDeal houseDeal : houseDealInfosFromOpenAPI) {
      if (!persistedHousesMap.containsKey(houseDeal.getHouse())) {
        newHouses.putIfAbsent(houseDeal.getHouse(), houseDeal.getHouse());
        if (newHouses.containsKey(houseDeal.getHouse())) {
          houseDeal.setPersistedHouse(newHouses.get(houseDeal.getHouse()));
        }
      } else {
        houseDeal.setPersistedHouse(persistedHousesMap.get(houseDeal.getHouse()));
      }

      if (!persistedHouseDeals.contains(houseDeal)) {
        newHouseDeals.add(houseDeal);
      }
    }

    houseRepository.saveAll(newHouses.keySet());
    houseDealRepository.saveAll(newHouseDeals);

    return new int[]{newHouses.size(), newHouseDeals.size()};
  }

  @Override
  public AverageDealsInRange getDealsByCodeAndDateRange(String code,
      Long houseId, DateRange dateRange) {
    LocalDate fromDate = LocalDate.of(dateRange.getFromYear(),
        dateRange.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dateRange.getToYear(),
        dateRange.getToMonth(), 1).with(lastDayOfMonth());
    List<AveragePricePerUnit> houseAveragePrice =
        houseDealRepository.findHouseAveragePriceByCodeAndDateRange(code, houseId,
            fromDate, toDate, dateRange.getType());
    return new AverageDealsInRange(dateRange, houseAveragePrice);
  }

  @Override
  public List<HouseDeal> getDealOfApt(Long hosueId) {
	  List<HouseDeal> dealList = houseDealRepository.findByHouseId(hosueId);
	  return dealList;
  }

  @Override
  public List<House> getHousesInArea(String code) {
    return houseRepository.findByCodeStartingWith(code);
  }

}
