package com.ssafy.happyhouse.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.area.Sido;
import com.ssafy.happyhouse.model.area.Sigugun;
import com.ssafy.happyhouse.model.area.Upmyundong;

@Mapper
public interface AreaRepository {

	List<Sido> findAllSidoes();
	List<Sigugun> findAllSiguguns(int sidoId);
	List<Upmyundong> findAllUpmyundongs(int sigugunId);
}
