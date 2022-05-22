package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.util.List;

public interface DealInfoRepositoryCustom {

  List<DealInfo> findByCodeYearMonthWithHouseInfo(String code, int dealYear, int dealMonth);

  List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(String code, long houseId,
      int fromYear, int toYear, int fromMonth, int toMonth, String type);
}
