package com.ssafy.happyhouse.dto.request;

import lombok.Data;

@Data
public class DealUpdateDto {
  private String code;
  private int fromYear;
  private int fromMonth;
  private int toYear;
  private int toMonth;
  private long umdId;
}
