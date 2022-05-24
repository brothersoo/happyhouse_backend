package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.time.LocalDate;
import java.util.List;

public interface HouseDealRepositoryCustom {

  List<HouseDeal> findByCodeAndYearMonthOfDate(String code, LocalDate date);

  List<HouseDeal> findByCodeAndDateBetween(String code, LocalDate fromDate, LocalDate toDate);

  List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(List<Long> houseIds,
      LocalDate fromDate, LocalDate toDate, String type);
}
