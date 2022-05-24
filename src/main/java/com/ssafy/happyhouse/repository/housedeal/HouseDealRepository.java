package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HouseDealRepository
    extends JpaRepository<HouseDeal, Long>, HouseDealRepositoryCustom { 
	
	@Query("SELECT d FROM HouseDeal d WHERE house_id = :houseId ORDER BY deal_date DESC")
	  List<HouseDeal> findByHouseId(@Param("houseId") Long houseId);
}
