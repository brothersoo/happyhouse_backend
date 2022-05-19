package com.ssafy.happyhouse.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.happyhouse.model.house.DealInfo;
import com.ssafy.happyhouse.model.house.HouseDeal;
import com.ssafy.happyhouse.model.house.HouseInfo;

@Mapper
public interface HouseRepository {

	Set<HouseInfo> findAllHouses();
	
	int insertBatchHouse(Collection<HouseInfo> houses);
	
	int insertBatchDeal(Collection<DealInfo> deals);

	List<DealInfo> findByAreaCodeDate(@Param("code") String code,
			@Param("dealYear") int dealYear, @Param("dealMonth") int dealMonth);
}
