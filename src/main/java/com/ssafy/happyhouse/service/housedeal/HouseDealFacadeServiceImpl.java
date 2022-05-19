package com.ssafy.happyhouse.service.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import com.ssafy.happyhouse.repository.housedeal.DealInfoRepository;
import com.ssafy.happyhouse.repository.housedeal.HouseInfoRepository;
import com.ssafy.happyhouse.util.housedeal.HouseDealAPIHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@RequiredArgsConstructor
@Service
public class HouseDealFacadeServiceImpl implements HouseDealFacadeService {

  private final DealInfoRepository dealInfoRepository;

  private final HouseInfoRepository houseInfoRepository;

  private HouseDealAPIHandler houseDealAPIHandler = HouseDealAPIHandler.getInstance();

  @Override
  public List<DealInfo> getDealsByCodeDate(String code, int dealYear, int dealMonth) {
    List<DealInfo> dealList = dealInfoRepository.findByCodeYearMonthWithHouseInfo(
        code, dealYear, dealMonth);

//    if (order.equals("aptName")) {
//      Collections.sort(dealList, (deal1, deal2) -> {
//        return deal1.getHouseInfo().getAptName().compareTo(deal2.getHouseInfo().getAptName());
//      });
//    } else if (order.equals("dealDate")) {
//      Collections.sort(dealList, (deal1, deal2) -> {
//        return deal1.getDealDay() - deal2.getDealDay();
//      });
//    } else if (order.equals("-dealDate")) {
//      Collections.sort(dealList, (deal1, deal2) -> {
//        return deal2.getDealDay() - deal1.getDealDay();
//      });
//    }
    return dealList;
  }

  @Override
  public int[] updateDeal(String code, int year, int month, Long upmyundongId)
      throws IOException, ParserConfigurationException, SAXException {
    List<DealInfo> dealInfosFromOpenAPI
        = houseDealAPIHandler.getMonthlyAreaDealInfo(code, year, month, upmyundongId);
    Set<DealInfo> dealInfosFromDB
        = new HashSet<>(dealInfoRepository.findByCodeYearMonthWithHouseInfo(code, year, month));
    Set<HouseInfo> houseInfosFromDB
        = new HashSet<>(houseInfoRepository.findByUpmyundongId(upmyundongId));

    List<DealInfo> newDealInfos = new ArrayList<>();
    List<HouseInfo> newHouseInfos = new ArrayList<>();
    for (DealInfo dealInfo : dealInfosFromOpenAPI) {
      if (!dealInfosFromDB.contains(dealInfo)) {
        newDealInfos.add(dealInfo);
      }
      if (!houseInfosFromDB.contains(dealInfo.getHouseInfo())) {
        newHouseInfos.add(dealInfo.getHouseInfo());
      }
    }

    houseInfoRepository.saveAll(newHouseInfos);
    dealInfoRepository.saveAll(newDealInfos);

    return new int[]{newHouseInfos.size(), newDealInfos.size()};
  }
}
