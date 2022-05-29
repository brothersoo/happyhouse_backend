package com.ssafy.happyhouse.service.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HousesAndDeals;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HouseService {

  List<House> getHousesInUpmyundong(Long upmyundongId);

  Set<House> getHouseSetInSigugun(List<String> codes);

  Map<House, House> getHouseMapInSigugun(List<String> codes);

  List<House> saveAll(Collection<House> houses);

  int batchInsert(List<House> houses);

  void setPersistedHouse(List<House> houses, HousesAndDeals housesAndDeals);
}
