package com.ssafy.happyhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AveragePricePerUnit {

  private String name;
  private int date;
  private double avgPrice;
  private long dealNumber;
}
