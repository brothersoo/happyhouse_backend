package com.ssafy.happyhouse.service.housedeal;

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
    return dealInfoRepository.findByCodeYearMonthWithHouseInfo(
        code, dealYear, dealMonth);
  }

  @Override
  @Transactional
  public int[] updateDeal(DealUpdateDto dealUpdateDto)
      throws IOException, ParserConfigurationException, SAXException {
    List<DealInfo> dealInfosFromOpenAPI = new ArrayList<>();
    Set<DealInfo> persistedDealInfos = new HashSet<>();

    List<HouseInfo> persistedHouseInfos
        = houseInfoRepository.findByUpmyundongId(dealUpdateDto.getUmdId());
    Map<HouseInfo, HouseInfo> persistedHouseInfosMap = new HashMap<>();
    for (HouseInfo houseInfo : persistedHouseInfos) {
      persistedHouseInfosMap.put(houseInfo, houseInfo);
    }

    for (int year = dealUpdateDto.getFromYear(); year <= dealUpdateDto.getToYear(); year++) {
      int fromMonth = (year == dealUpdateDto.getFromYear()) ? dealUpdateDto.getFromMonth() : 1;
      int toMonth = (year == dealUpdateDto.getToYear()) ? dealUpdateDto.getToMonth() : 12;
      for (int month = fromMonth; month <= toMonth; month++) {
        dealInfosFromOpenAPI.addAll(
            houseDealAPIHandler.getMonthlyAreaDealInfo(dealUpdateDto.getCode(), year, month,
                dealUpdateDto.getUmdId()));
        persistedDealInfos.addAll(dealInfoRepository.findByCodeYearMonthWithHouseInfo(
            dealUpdateDto.getCode(), year, month));
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
    List<AveragePricePerUnit> houseAveragePrice =
        dealInfoRepository.findHouseAveragePriceByCodeAndDateRange(code, houseId,
            dateRange.getFromYear(), dateRange.getToYear(),
            dateRange.getFromMonth(), dateRange.getToMonth(), dateRange.getType());
    return new AverageDealsInRange(dateRange, houseAveragePrice);
  }

  @Override
  public List<HouseInfo> getHouseInfosInArea(String code) {
    return houseInfoRepository.findByCodeStartingWith(code);
  }
}
