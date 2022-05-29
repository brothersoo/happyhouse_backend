package com.ssafy.happyhouse.repository.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import java.util.List;

public interface HouseRepositoryCustom {

  List<House> findByCodeStartingWithIn(List<String> codes);

  int batchInsert(List<House> houses);

  List<House> findByGroupOfNamesIn(List<House> houses);
}
