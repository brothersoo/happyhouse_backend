package com.ssafy.happyhouse.repository.updateddealinfo;

import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import java.time.LocalDate;
import java.util.List;

public interface UpdatedDealInfoRepositoryCustom {

  List<UpdatedDealInfo> findBySigugunCodeInAndDateBetween(List<String> sigugunCodes, LocalDate fromDate, LocalDate toDate);
}
