//package com.ssafy.happyhouse.service.housedeal;
//
//import com.ssafy.happyhouse.domain.housedeal.DealInfo;
//import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
//import com.ssafy.happyhouse.repository.housedeal.DealInfoRepository;
//import com.ssafy.happyhouse.util.housedeal.HouseDealAPIHandler;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.xml.parsers.ParserConfigurationException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.xml.sax.SAXException;
//
//@RequiredArgsConstructor
//@Service
//public class DealInfoServiceImpl implements DealInfoService{
//
//  private HouseDealAPIHandler houseDealAPIHandler = HouseDealAPIHandler.getInstance();
//  private final DealInfoRepository dealInfoRepository;
//
//  public int[] updateDeal(String code, int year, int month, Long upmyundongId)
//      throws IOException, ParserConfigurationException, SAXException {
//    List<DealInfo> dealInfosFromOpenAPI
//        = houseDealAPIHandler.getMonthlyAreaDealInfo(code, year, month, upmyundongId);
//
//    List<HouseInfo> persistedHouseInfos
//        = houseInfoRepository.findByUpmyundongId(upmyundongId);
//    Map<HouseInfo, HouseInfo> persistedHouseInfosMap = new HashMap<>();
//    for (HouseInfo houseInfo : persistedHouseInfos) {
//      persistedHouseInfosMap.put(houseInfo, houseInfo);
//    }
//
//    Set<DealInfo> persistedDealInfos
//        = new HashSet<>(dealInfoRepository.findByCodeYearMonthWithHouseInfo(code, year, month));
//
//    List<DealInfo> newDealInfos = new ArrayList<>();
//    Map<HouseInfo, HouseInfo> newHouseInfos = new HashMap<>();
//
//    for (DealInfo dealInfo : dealInfosFromOpenAPI) {
//      if (!persistedHouseInfosMap.containsKey(dealInfo.getHouseInfo())) {
//        newHouseInfos.putIfAbsent(dealInfo.getHouseInfo(), dealInfo.getHouseInfo());
//        if (newHouseInfos.containsKey(dealInfo.getHouseInfo())) {
//          dealInfo.setPersistedHouseInfo(newHouseInfos.get(dealInfo.getHouseInfo()));
//        }
//      } else {
//        dealInfo.setPersistedHouseInfo(persistedHouseInfosMap.get(dealInfo.getHouseInfo()));
//      }
//
//      if (!persistedDealInfos.contains(dealInfo)) {
//        newDealInfos.add(dealInfo);
//      }
//    }
//
//    houseInfoRepository.saveAll(newHouseInfos.keySet());
//    dealInfoRepository.saveAll(newDealInfos);
//
//    return new int[]{newHouseInfos.size(), newDealInfos.size()};
//  }
//}
