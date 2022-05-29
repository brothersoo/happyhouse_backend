package com.ssafy.happyhouse.service.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.repository.house.HouseRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HouseServiceImpl implements HouseService {

  private final HouseRepository houseRepository;

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
}
