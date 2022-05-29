package com.ssafy.happyhouse.service.updateddealinfo;

import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UpdatedDealInfoService {

  Set<UpdatedDealInfo> findByCodeInAndDateBetweenSet(
      List<String> codes, LocalDate fromDate, LocalDate toDate);

  List<UpdatedDealInfo> saveAll(List<UpdatedDealInfo> updatedDealInfo);
}
