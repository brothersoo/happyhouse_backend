package com.ssafy.happyhouse.repository.house;

import com.ssafy.happyhouse.domain.housedeal.House;
import java.util.List;

public interface HouseRepositoryCustom {

  int batchInsert(List<House> houses);
}
