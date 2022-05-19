package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DealInfoRepository extends JpaRepository<DealInfo, Long> {

  @Query("SELECT d FROM DealInfo d "
      + "JOIN d.houseInfo h "
      + "JOIN h.upmyundong u "
      + "WHERE u.code LIKE :code% AND d.dealYear=:dealYear AND d.dealMonth=:dealMonth")
  List<DealInfo> findByCodeYearMonthWithHouseInfo(String code, int dealYear, int dealMonth);
}
