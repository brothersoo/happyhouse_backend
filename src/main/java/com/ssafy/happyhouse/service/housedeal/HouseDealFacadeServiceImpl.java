package com.ssafy.happyhouse.service.housedeal;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.AverageDealsInRange;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.repository.housedeal.DealInfoRepository;
import com.ssafy.happyhouse.repository.housedeal.HouseInfoRepository;
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
  private final DealInfoRepository dealInfoRepository;
  private final HouseInfoRepository houseInfoRepository;

  @Override
  public List<DealInfo> getDealsByCodeDate(String code, int dealYear, int dealMonth) {
    return dealInfoRepository.findByCodeAndYearMonthOfDate(
        code, LocalDate.of(dealYear, dealMonth, 1));
  }

  @Override
  @Transactional
  public int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    List<DealInfo> dealInfosFromOpenAPI = new ArrayList<>();

    // get persisted house infos
    List<HouseInfo> persistedHouseInfos
        = houseInfoRepository.findByUpmyundongId(dealUpdateDto.getUmdId());
    Map<HouseInfo, HouseInfo> persistedHouseInfosMap = new HashMap<>();
    for (HouseInfo houseInfo : persistedHouseInfos) {
      persistedHouseInfosMap.put(houseInfo, houseInfo);
    }

    // get persisted deal infos
    LocalDate fromDate = LocalDate.of(dealUpdateDto.getFromYear(), dealUpdateDto.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dealUpdateDto.getToYear(),
        dealUpdateDto.getToMonth(), 1).with(lastDayOfMonth());
    Set<DealInfo> persistedDealInfos = new HashSet<>(
        dealInfoRepository.findByCodeAndDateBetween(
            dealUpdateDto.getCode(), fromDate, toDate));

    // api call in date range (monthly)
    for (int year = dealUpdateDto.getFromYear(); year <= dealUpdateDto.getToYear(); year++) {
      int fromMonth = (year == dealUpdateDto.getFromYear()) ? dealUpdateDto.getFromMonth() : 1;
      int toMonth = (year == dealUpdateDto.getToYear()) ? dealUpdateDto.getToMonth() : 12;
      for (int month = fromMonth; month <= toMonth; month++) {
        dealInfosFromOpenAPI.addAll(
            houseDealAPIHandler.getMonthlyAreaDealInfo(dealUpdateDto.getCode(), year, month,
                dealUpdateDto.getUmdId()));
      }
    }

    List<DealInfo> newDealInfos = new ArrayList<>();
    Map<HouseInfo, HouseInfo> newHouseInfos = new HashMap<>();

    for (DealInfo dealInfo : dealInfosFromOpenAPI) {
      if (!persistedHouseInfosMap.containsKey(dealInfo.getHouseInfo())) {
        newHouseInfos.putIfAbsent(dealInfo.getHouseInfo(), dealInfo.getHouseInfo());
        if (newHouseInfos.containsKey(dealInfo.getHouseInfo())) {
          dealInfo.setPersistedHouseInfo(newHouseInfos.get(dealInfo.getHouseInfo()));
        }
      } else {
        dealInfo.setPersistedHouseInfo(persistedHouseInfosMap.get(dealInfo.getHouseInfo()));
      }

      if (!persistedDealInfos.contains(dealInfo)) {
        newDealInfos.add(dealInfo);
      }
    }

    houseInfoRepository.saveAll(newHouseInfos.keySet());
    dealInfoRepository.saveAll(newDealInfos);

    return new int[]{newHouseInfos.size(), newDealInfos.size()};
  }

  @Override
  public AverageDealsInRange getDealsByCodeAndDateRange(String code,
      Long houseId, DateRange dateRange) {
    LocalDate fromDate = LocalDate.of(dateRange.getFromYear(),
        dateRange.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dateRange.getToYear(),
        dateRange.getToMonth(), 1).with(lastDayOfMonth());
    List<AveragePricePerUnit> houseAveragePrice =
        dealInfoRepository.findHouseAveragePriceByCodeAndDateRange(code, houseId,
            fromDate, toDate, dateRange.getType());
    return new AverageDealsInRange(dateRange, houseAveragePrice);
  }

  @Override
  public List<HouseInfo> getHouseInfosInArea(String code) {
    return houseInfoRepository.findByCodeStartingWith(code);
  }
}
