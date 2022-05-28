package com.ssafy.happyhouse.repository.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseRepository extends JpaRepository<House, Long>, HouseRepositoryCustom {

  @Query("SELECT h FROM House h WHERE upmyundong_id = :upmyundongId")
  List<House> findByUpmyundongId(@Param("upmyundongId") Long upmyundongId);

  @Query("SELECT h FROM House h JOIN h.upmyundong u WHERE u.code LIKE :code%")
  List<House> findByCodeStartingWith(@Param("code") String code);
}
