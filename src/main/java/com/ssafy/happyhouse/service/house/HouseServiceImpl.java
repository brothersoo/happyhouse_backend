package com.ssafy.happyhouse.service.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.HousesAndDeals;
import com.ssafy.happyhouse.repository.house.HouseRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HouseServiceImpl implements HouseService {

  private final HouseRepository houseRepository;

  @Override
  public List<House> getHousesInUpmyundong(Long upmyundongId) {
    return houseRepository.findByUpmyundongId(upmyundongId);
  }

  @Override
  public Set<House> getHouseSetInSigugun(List<String> codes) {
    return new HashSet<>(houseRepository.findByCodeStartingWithIn(codes));
  }

  @Override
  public Map<House, House> getHouseMapInSigugun(List<String> codes) {
    Map<House, House> houseMap = new HashMap<>();
    for (House house : houseRepository.findByCodeStartingWithIn(codes)) {
      houseMap.putIfAbsent(house, house);
    }
    return houseMap;
  }

  @Override
  @Transactional
  public List<House> saveAll(Collection<House> houses) {
    return houseRepository.saveAll(houses);
  }

  @Override
  @Transactional
  public int batchInsert(List<House> houses) {
    return houseRepository.batchInsert(houses);
  }

  @Override
  public void setPersistedHouse(List<House> houses, HousesAndDeals housesAndDeals) {
    Map<House, House> houseMap = new HashMap<>();
    List<House> persistedHouses = houseRepository.findByGroupOfNamesIn(houses);
    for (House house : persistedHouses) {
      houseMap.putIfAbsent(house, house);
    }
    for (HouseDeal houseDeal : housesAndDeals.getHouseDeals()) {
      houseDeal.setPersistedHouse(houseMap.get(houseDeal.getHouse()));
    }
  }
}
