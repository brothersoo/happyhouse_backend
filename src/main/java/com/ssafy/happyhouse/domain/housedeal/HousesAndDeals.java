package com.ssafy.happyhouse.domain.housedeal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HousesAndDeals {

  private List<House> houses;
  private List<HouseDeal> houseDeals;
}
