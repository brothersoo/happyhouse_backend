package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseInfoRepository extends JpaRepository<HouseInfo, Long> {

  @Query("SELECT h FROM HouseInfo h WHERE upmyundong_id = :upmyundongId")
  List<HouseInfo> findByUpmyundongId(@Param("upmyundongId") Long upmyundongId);

  @Query("SELECT h FROM HouseInfo h JOIN h.upmyundong u WHERE u.code LIKE :code%")
  List<HouseInfo> findByCodeStartingWith(@Param("code") String code);
}
