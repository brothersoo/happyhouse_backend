package com.ssafy.happyhouse.dto.response.graph;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChartData {
  // x축 라벨
  private List<String> labels;

  private List<Dataset> datasets;
}