package com.ssafy.happyhouse.unit.service;

import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.dto.response.graph.Dataset;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import com.ssafy.happyhouse.service.housedeal.HouseDealFacadeServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseDealServiceTest {

  @Mock
  private HouseDealRepository houseDealRepository;
  @InjectMocks
  private HouseDealFacadeServiceImpl houseDealService;

  @Test
  void chartDataTest() {
    List<AveragePricePerUnit> averagePricePerUnits = new ArrayList<>();
    averagePricePerUnits.add(new AveragePricePerUnit("apt1", "2022-01", 1, 1));
    averagePricePerUnits.add(new AveragePricePerUnit("apt1", "2022-02", 2, 2));
    averagePricePerUnits.add(new AveragePricePerUnit("apt1", "2022-03", 3, 3));
    averagePricePerUnits.add(new AveragePricePerUnit("apt1", "2022-04", 4, 4));
    averagePricePerUnits.add(new AveragePricePerUnit("apt1", "2022-07", 7, 7));

    averagePricePerUnits.add(new AveragePricePerUnit("apt2", "2022-02", 2, 2));
    averagePricePerUnits.add(new AveragePricePerUnit("apt2", "2022-03", 3, 3));
    averagePricePerUnits.add(new AveragePricePerUnit("apt2", "2022-05", 5, 5));
    averagePricePerUnits.add(new AveragePricePerUnit("apt2", "2022-06", 6, 6));

    List<Long> houseIds = new ArrayList<>();
    houseIds.add(1L);
    houseIds.add(2L);
    Mockito
        .when(houseDealRepository.findHouseAveragePriceByCodeAndDateRange(
            houseIds, LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 6, 30), "month"
        ))
        .thenReturn(averagePricePerUnits);

    ChartData chartData = houseDealService.getChartDataOfHouses(houseIds,
        new DateRange("month",2022, 2022, 1, 6));

    List<String> expectedLabels = new ArrayList<>();
    for (int i = 1; i <= 7; i++) {
      String label = "2022-0" + i;
      expectedLabels.add(label);
    }
    List<Double> expectedApt1Data = new ArrayList<>();
    expectedApt1Data.add(1D);
    expectedApt1Data.add(2D);
    expectedApt1Data.add(3D);
    expectedApt1Data.add(4D);
    expectedApt1Data.add(4D);
    expectedApt1Data.add(7D);
    expectedApt1Data.add(7D);
    List<Double> expectedApt2Data = new ArrayList<>();
    expectedApt2Data.add(2D);
    expectedApt2Data.add(2D);
    expectedApt2Data.add(3D);
    expectedApt2Data.add(3D);
    expectedApt2Data.add(5D);
    expectedApt2Data.add(6D);
    expectedApt2Data.add(6D);

    Assertions.assertThat(chartData.getLabels()).isEqualTo(expectedLabels);
    List<String> sortedLabels = new ArrayList<>(chartData.getLabels());
    sortedLabels.sort(null);
    Assertions.assertThat(chartData.getLabels()).isEqualTo(sortedLabels);

    Assertions.assertThat(chartData.getDatasets()).hasSize(2);
    for (Dataset dataset : chartData.getDatasets()) {
      Assertions.assertThat(dataset.getData()).hasSize(chartData.getLabels().size());
      if (dataset.getLabel().equals("apt1")) {
        Assertions.assertThat(dataset.getData()).isEqualTo(expectedApt1Data);
      } else if (dataset.getLabel().equals("apt2")) {
        Assertions.assertThat(dataset.getData()).isEqualTo(expectedApt2Data);
      }
    }
  }
}
