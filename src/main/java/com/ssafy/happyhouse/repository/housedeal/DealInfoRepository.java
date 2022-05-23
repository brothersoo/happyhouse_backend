package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DealInfoRepository
    extends JpaRepository<DealInfo, Long>, DealInfoRepositoryCustom { 
	
	@Query("SELECT d FROM DealInfo d WHERE house_info_id = :houseId")
	  List<DealInfo> findByHouseId(@Param("houseId") Long houseId);

}
