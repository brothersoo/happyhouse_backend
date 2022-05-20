package com.ssafy.happyhouse.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class AverageDealsInRange  {

  private DateRange dateRange;
  List<AveragePricePerUnit> deals;

  public AverageDealsInRange(DateRange dateRange, List<AveragePricePerUnit> averagePricePerUnits) {
    this.dateRange = dateRange;
    this.deals = averagePricePerUnits;
  }
}
