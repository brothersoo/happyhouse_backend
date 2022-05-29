package com.ssafy.happyhouse.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class DealUpdateDto {
  private List<String> codes;
  private int fromYear;
  private int fromMonth;
  private int toYear;
  private int toMonth;
}
