package com.ssafy.happyhouse.dto.response.graph;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Dataset {
  // 아파트명
  private String label;

  // 월별 평균 거래가
  List<Double> data;
}
