package com.ssafy.happyhouse.dto.response;

import lombok.Data;

@Data
public class HouseDealFromAPI {

  private int price;
  private String type;
  private int buildYear;
  private int dealYear;
  private int dealMonth;
  private int dealDay;
  private String upmyundongName;
  private String aptName;
  private Float exclusivePrivateArea;
  private String jibun;
  private int floor;
}
