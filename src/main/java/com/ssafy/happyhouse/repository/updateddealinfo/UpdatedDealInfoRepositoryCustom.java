package com.ssafy.happyhouse.repository.updateddealinfo;

import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import java.time.LocalDate;
import java.util.List;

public interface UpdatedDealInfoRepositoryCustom {

  List<UpdatedDealInfo> findByCodeInAndDateBetween(List<String> code, LocalDate fromDate, LocalDate toDate);
}
