package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.time.LocalDate;
import java.util.List;

public interface DealInfoRepositoryCustom {

  List<DealInfo> findByCodeAndYearMonthOfDate(String code, LocalDate date);

  List<DealInfo> findByCodeAndDateBetween(String code, LocalDate fromDate, LocalDate toDate);

  List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(String code, long houseId,
      LocalDate fromDate, LocalDate toDate, String type);
}
