package com.ssafy.happyhouse.unit.service;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.dto.response.graph.Dataset;
import com.ssafy.happyhouse.repository.house.HouseRepository;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import com.ssafy.happyhouse.service.area.AreaFacadeService;
import com.ssafy.happyhouse.service.housedeal.HouseDealFacadeServiceImpl;
import com.ssafy.happyhouse.util.housedeal.HouseDealAPIHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.xml.sax.SAXException;

@ExtendWith(MockitoExtension.class)
class HouseDealServiceTest {

  @Mock
  private HouseDealRepository houseDealRepository;
  @Mock
  private HouseRepository houseRepository;
  @Mock
  private HouseDealAPIHandler houseDealAPIHandler;
  @Mock
  private AreaFacadeService areaFacadeService;
  @InjectMocks
  private HouseDealFacadeServiceImpl houseDealService;

  @Test
  @DisplayName("그래프 데이터 생성")
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

  @Test
  @DisplayName("거래 목록 갱신 테스트")
  void updateHouseDeal() throws IOException, ParserConfigurationException, SAXException {
    DealUpdateDto dealUpdateDto = new DealUpdateDto();
    dealUpdateDto.setCode("abc");
    dealUpdateDto.setFromYear(2020);
    dealUpdateDto.setFromMonth(1);
    dealUpdateDto.setToYear(2022);
    dealUpdateDto.setToMonth(3);

    Upmyundong 사당동 = Upmyundong.builder().name("사당동").build();
    Upmyundong 삼평동 = Upmyundong.builder().name("삼평동").build();
    Upmyundong 상왕십리동 = Upmyundong.builder().name("상왕십리동").build();

    House 금오아울림 = House.builder().aptName("금오아울림").upmyundong(삼평동).build();
    House 진응 = House.builder().aptName("진응").upmyundong(삼평동).build();
    House 파랗지오 = House.builder().aptName("파랗지오").upmyundong(사당동).build();
    House 쩔어아파트 = House.builder().aptName("쩔어아파트").upmyundong(사당동).build();
    House 텐즈휠 = House.builder().aptName("텐즈휠").upmyundong(상왕십리동).build();
    House 벽솬 = House.builder().aptName("벽솬").upmyundong(상왕십리동).build();

    HouseDeal deal1 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2020, 1, 3)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal2 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2020, 3, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal3 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2021, 5, 1)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal4 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2021, 10, 8)).type("A").floor(1).price(12000).exclusivePrivateArea(1).build();
    HouseDeal deal5 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 1, 21)).type("A").floor(1).price(12000).exclusivePrivateArea(1).build();
    HouseDeal deal6 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2020, 3, 11)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal7 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2021, 4, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal8 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2022, 3, 17)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal9 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2020, 5, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal10 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2020, 10, 1)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal11 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2022, 3, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal12 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2021, 10, 8)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal13 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 2, 1)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal14 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 2, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal15 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 3, 19)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal16 = HouseDeal.builder().house(벽솬).dealDate(LocalDate.of(2020, 7, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal17 = HouseDeal.builder().house(벽솬).dealDate(LocalDate.of(2020, 11, 8)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();
    HouseDeal deal18 = HouseDeal.builder().house(쩔어아파트).dealDate(LocalDate.of(2022, 2, 10)).type("A").floor(1).price(10000).exclusivePrivateArea(1).build();

    List<House> persistedHouses = Arrays.asList(금오아울림, 진응, 파랗지오, 텐즈휠, 벽솬);
    List<HouseDeal> persistedHouseDeals = Arrays.asList(
        deal1, deal2, deal3, deal4, deal6, deal7, deal9, deal10, deal12, deal16, deal17);

    List<HouseDeal> houseDealsAPI202001 = Arrays.asList(deal1);
    List<HouseDeal> houseDealsAPI202003 = Arrays.asList(deal2, deal6);
    List<HouseDeal> houseDealsAPI202005 = Arrays.asList(deal9);
    List<HouseDeal> houseDealsAPI202007 = Arrays.asList(deal16);
    List<HouseDeal> houseDealsAPI202010 = Arrays.asList(deal10);
    List<HouseDeal> houseDealsAPI202011 = Arrays.asList(deal17);
    List<HouseDeal> houseDealsAPI202104 = Arrays.asList(deal7);
    List<HouseDeal> houseDealsAPI202105 = Arrays.asList(deal3);
    List<HouseDeal> houseDealsAPI202110 = Arrays.asList(deal4);
    List<HouseDeal> houseDealsAPI202201 = Arrays.asList(deal5);
    List<HouseDeal> houseDealsAPI202202 = Arrays.asList(deal13, deal14, deal18);
    List<HouseDeal> houseDealsAPI202203 = Arrays.asList(deal8, deal11, deal15);

    Mockito
        .when(houseRepository.findByCodeStartingWith(dealUpdateDto.getCode()))
        .thenReturn(persistedHouses);
    Mockito
        .when(houseDealRepository.findByCodeAndDateBetween(
            "abc", LocalDate.of(2020, 1, 1),
            LocalDate.of(2022, 3, 31)
        ))
        .thenReturn(persistedHouseDeals);

    List<HouseDeal> emptyList = new ArrayList<>();

    Mockito
        .doReturn(emptyList)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    Mockito
        .doReturn(houseDealsAPI202001)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 1);
    Mockito
        .doReturn(houseDealsAPI202003)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 3);
    Mockito
        .doReturn(houseDealsAPI202005)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 5);
    Mockito
        .doReturn(houseDealsAPI202007)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 7);
    Mockito
        .doReturn(houseDealsAPI202010)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 10);
    Mockito
        .doReturn(houseDealsAPI202011)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2020, 11);
    Mockito
        .doReturn(houseDealsAPI202104)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2021, 4);
    Mockito
        .doReturn(houseDealsAPI202105)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2021, 5);
    Mockito
        .doReturn(houseDealsAPI202110)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2021, 10);
    Mockito
        .doReturn(houseDealsAPI202201)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2022, 1);
    Mockito
        .doReturn(houseDealsAPI202202)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2022, 2);
    Mockito
        .doReturn(houseDealsAPI202203)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2022, 3);

    Mockito
        .doReturn(Arrays.asList(삼평동, 사당동, 상왕십리동))
        .when(areaFacadeService)
        .searchUpmyundongsInSigugun("abc");

    Mockito
        .when(houseRepository.saveAll(Mockito.anySet()))
        .thenAnswer(new Answer<List<House>>() {
          @Override
          public List<House> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return new ArrayList<>((Set)args[0]);
          }
        });
    Mockito
        .when(houseDealRepository.saveAll(Mockito.anyList()))
        .thenAnswer(new Answer<List<HouseDeal>>() {
          @Override
          public List<HouseDeal> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return (List)args[0];
          }
        });

    List<HouseDeal> houseDealsIn2022 = Arrays.asList(
        deal5, deal8, deal11, deal13, deal14, deal15, deal18);

    int[] res = houseDealService.updateDeal(dealUpdateDto);

    Assertions.assertThat(res[0]).isEqualTo(1);
    Assertions.assertThat(res[1]).isEqualTo(houseDealsIn2022.size());
  }
}
