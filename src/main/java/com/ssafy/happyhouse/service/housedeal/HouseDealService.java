package com.ssafy.happyhouse.service.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface HouseDealService {

  Set<HouseDeal> getHouseDealSetInSigugunBetweenDate(
      List<String> codes, LocalDate fromDate, LocalDate toDate);

  List<HouseDeal> saveAll(Collection<HouseDeal> houseDeals);

  List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(List<Long> houseIds,
      LocalDate fromDate, LocalDate toDate, String type);
}
