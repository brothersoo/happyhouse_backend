package com.ssafy.happyhouse.service.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HouseService {

  Map<House, House> getHouseMapInSigugun(List<String> codes);

  List<House> saveAll(Collection<House> houses);
}
