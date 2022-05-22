package com.ssafy.happyhouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DateRange {

  private String type;
  private Integer fromYear;
  private Integer toYear;
  private Integer fromMonth;
  private Integer toMonth;
}
