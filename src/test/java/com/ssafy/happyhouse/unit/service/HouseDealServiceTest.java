package com.ssafy.happyhouse.unit.service;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.dto.response.graph.Dataset;
import com.ssafy.happyhouse.service.area.sigugun.SigugunService;
import com.ssafy.happyhouse.service.area.upmyundong.UpmyundongService;
import com.ssafy.happyhouse.service.house.HouseService;
import com.ssafy.happyhouse.service.facade.HouseDealFacadeServiceImpl;
import com.ssafy.happyhouse.service.housedeal.HouseDealService;
import com.ssafy.happyhouse.service.updateddealinfo.UpdatedDealInfoService;
import com.ssafy.happyhouse.util.housedeal.HouseDealAPIHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
  private SigugunService sigugunService;
  @Mock
  private UpmyundongService upmyundongService;
  @Mock
  private HouseService houseService;
  @Mock
  private HouseDealService houseDealService;
  @Mock
  private HouseDealAPIHandler houseDealAPIHandler;
  @Mock
  private UpdatedDealInfoService updatedDealInfoService;
  @InjectMocks
  private HouseDealFacadeServiceImpl houseDealFacadeService;

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
        .when(houseDealService.findHouseAveragePriceByCodeAndDateRange(
            houseIds, LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 6, 30), "month"
        ))
        .thenReturn(averagePricePerUnits);

    ChartData chartData = houseDealFacadeService.getChartDataOfHouses(houseIds,
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
    dealUpdateDto.setCodes(Arrays.asList("abc", "가나다"));
    dealUpdateDto.setFromYear(2020);
    dealUpdateDto.setFromMonth(1);
    dealUpdateDto.setToYear(2022);
    dealUpdateDto.setToMonth(4);

    LocalDate fromDate = LocalDate.of(dealUpdateDto.getFromYear(), dealUpdateDto.getFromMonth(), 1);
    LocalDate toDate = LocalDate.of(dealUpdateDto.getToYear(), dealUpdateDto.getToMonth(), 1)
        .with(lastDayOfMonth());

    Sigugun abc = Sigugun.builder().code("abc").build();
    Sigugun 가나다 = Sigugun.builder().code("가나다").build();
    Map<String, Sigugun> codeSigugunMap = new HashMap<>();
    for (Sigugun sigugun : Arrays.asList(abc, 가나다)) {
      codeSigugunMap.putIfAbsent(sigugun.getCode(), sigugun);
    }

    Upmyundong 삼평동 = Upmyundong.builder().name("삼평동").code("abc1").sigugun(abc).build();
    Upmyundong 사당동 = Upmyundong.builder().name("사당동").code("abc2").sigugun(abc).build();
    Upmyundong 상왕십리동 = Upmyundong.builder().name("상왕십리동").code("가나다1").sigugun(가나다).build();
    Map<String, Upmyundong> nameUpmyundongMap = new HashMap<>();
    for (Upmyundong upmyundong : Arrays.asList(삼평동, 사당동, 상왕십리동)) {
      nameUpmyundongMap.putIfAbsent(upmyundong.getName(), upmyundong);
    }

    House 금오아울림 = House.builder().id(1L).aptName("금오아울림").upmyundong(삼평동).build();
    House 진응 = House.builder().id(2L).aptName("진응").upmyundong(삼평동).build();
    House 텐즈휠 = House.builder().id(3L).aptName("텐즈휠").upmyundong(상왕십리동).build();
    House 벽솬 = House.builder().id(4L).aptName("벽솬").upmyundong(상왕십리동).build();
    House 파랗지오 = House.builder().id(5L).aptName("파랗지오").upmyundong(사당동).build();
    House 쩔어아파트 = House.builder().id(6L).aptName("쩔어아파트").upmyundong(사당동).build();

    // 2022년 4월 19일 이전 매매 데이터
    HouseDeal deal1 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 4, 3)).build();
    HouseDeal deal2 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 4, 10)).build();
    HouseDeal deal3 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 4, 11)).build();
    HouseDeal deal4 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 4, 17)).build();
    HouseDeal deal5 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2022, 4, 1)).build();
    HouseDeal deal6 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2022, 4, 3)).build();
    HouseDeal deal7 = HouseDeal.builder().house(진응).dealDate(LocalDate.of(2022, 4, 17)).build();
    HouseDeal deal8 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 4, 4)).build();
    HouseDeal deal9 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 4, 5)).build();
    HouseDeal deal10 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 4, 9)).build();
    HouseDeal deal11 = HouseDeal.builder().house(텐즈휠).dealDate(LocalDate.of(2022, 4, 19)).build();
    HouseDeal deal12 = HouseDeal.builder().house(벽솬).dealDate(LocalDate.of(2022, 4, 10)).build();
    HouseDeal deal13 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2022, 4, 10)).build();
    HouseDeal deal14 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2022, 4, 14)).build();

    // 2022년 4월 20일 이후 매매 데이터
    HouseDeal deal15 = HouseDeal.builder().house(금오아울림).dealDate(LocalDate.of(2022, 4, 20)).build();
    HouseDeal deal16 = HouseDeal.builder().house(벽솬).dealDate(LocalDate.of(2022, 4, 23)).build();
    HouseDeal deal17 = HouseDeal.builder().house(파랗지오).dealDate(LocalDate.of(2022, 4, 23)).build();
    HouseDeal deal18 = HouseDeal.builder().house(쩔어아파트).dealDate(LocalDate.of(2022, 4, 25)).build();
    HouseDeal deal19 = HouseDeal.builder().house(쩔어아파트).dealDate(LocalDate.of(2022, 4, 26)).build();
    HouseDeal deal20 = HouseDeal.builder().house(쩔어아파트).dealDate(LocalDate.of(2022, 4, 27)).build();

    // 쩔어아파트를 제외한 아파트들은 이미 데이터베이스에 저장되어있음
    Map<House, House> persistedHouseMap = new HashMap<>();
    for (House house : Arrays.asList(금오아울림, 진응, 파랗지오, 텐즈휠, 벽솬)) {
      persistedHouseMap.putIfAbsent(house, house);
    }
    // 2022년 1월 ~ 4월 19일까지의 매매 데이터들은 저장되어있음
    Set<HouseDeal> persistedHouseDeals = new HashSet<>(Arrays.asList(
        deal1, deal2, deal3, deal4, deal5, deal6, deal7,
        deal8, deal9, deal10, deal11, deal12, deal13, deal14
    ));

    List<HouseDeal> houseDealsInAPR = new ArrayList<>();
    houseDealsInAPR.add(deal15);
    houseDealsInAPR.add(deal16);
    houseDealsInAPR.add(deal17);
    houseDealsInAPR.add(deal18);
    houseDealsInAPR.add(deal19);
    houseDealsInAPR.add(deal20);
    houseDealsInAPR.addAll(persistedHouseDeals);

    // 저장된 houses stubbing
    Mockito
        .when(houseService.getHouseMapInSigugun(dealUpdateDto.getCodes()))
        .thenReturn(persistedHouseMap);
    // 저장된 매매 정보 stubbing
    Mockito
        .when(houseDealService.getHouseDealSetInSigugunBetweenDate(
            dealUpdateDto.getCodes(), fromDate, toDate))
        .thenReturn(persistedHouseDeals);

    // abc와 가나다의 2020년 1월 ~ 2022년 3월까지는 이미 정보를 갱신한 날짜로 지정
    List<UpdatedDealInfo> updatedDealInfosList = new ArrayList<>();
    for (LocalDate date = fromDate; date.isBefore(LocalDate.of(2022, 4, 1)); date = date.plusMonths(1)) {
      updatedDealInfosList.add(UpdatedDealInfo.builder().sigugun(abc).date(date).build());
      updatedDealInfosList.add(UpdatedDealInfo.builder().sigugun(가나다).date(date).build());
    }
    updatedDealInfosList.sort((UpdatedDealInfo a, UpdatedDealInfo b) -> {
      return b.getDate().compareTo(a.getDate());
    });
    Set<UpdatedDealInfo> updatedDealInfos = new HashSet<>(updatedDealInfosList);

    // 이미 갱신한 지역, 날짜 데이터 stubbing
    Mockito
        .doReturn(updatedDealInfos)
        .when(updatedDealInfoService)
        .findByCodeInAndDateBetweenSet(dealUpdateDto.getCodes(), fromDate, toDate);

    Mockito
        .doReturn(codeSigugunMap)
        .when(sigugunService)
        .getCodeSigugunMap(dealUpdateDto.getCodes());

    Mockito
        .doReturn(nameUpmyundongMap)
        .when(upmyundongService)
        .getNameUpmyundongMap(dealUpdateDto.getCodes());

    List<HouseDeal> emptyList = new ArrayList<>();

    // 지정된 날짜 이외에는 빈 리스트를 반환하도록 stubbing
    Mockito
        .doReturn(emptyList)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    // 지정된 날짜에 해당하는 매매 정보 stubbing (탐색된 지역구와 날짜는 제외)
    Mockito
        .doReturn(houseDealsInAPR)
        .when(houseDealAPIHandler)
        .getMonthlyAreaDealInfo("abc", 2022, 4);

    // UpdatedDealInfo saveAll() 메서드가 인자로 넘겨진 list의 size를 반환하도록 stubbing
    Mockito
        .when(updatedDealInfoService.saveAll(Mockito.anyList()))
        .thenAnswer(new Answer<List<House>>() {
          @Override
          public List<House> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return (List)args[0];
          }
        });
    // 아파트 saveAll() 메서드가 인자로 넘겨진 list의 size를 반환하도록 stubbing
    Mockito
        .when(houseService.saveAll(Mockito.anySet()))
        .thenAnswer(new Answer<List<House>>() {
          @Override
          public List<House> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return new ArrayList<>((Set)args[0]);
          }
        });
    // 매매 정보 saveAll() 메서드가 인자로 넘겨진 list의 size를 반환하도록 stubbing
    Mockito
        .when(houseDealService.saveAll(Mockito.anyList()))
        .thenAnswer(new Answer<List<HouseDeal>>() {
          @Override
          public List<HouseDeal> answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return (List)args[0];
          }
        });

    // when
    int[] res = houseDealFacadeService.updateDeal(dealUpdateDto);

    // then

    // 새로 저장된 아파트는 쩔어아파트 한개다.
    Assertions.assertThat(res[0]).isEqualTo(1);

    // 새로 저장된 매매 정보는 2022년 1월 부터 2022년 3월까지의 정보들이다. (houseDealsIn2022)
    Assertions.assertThat(res[1]).isEqualTo(houseDealsInAPR.size() - persistedHouseDeals.size());

    // 외부 api 호출은 1(4월달 한번) * 2(abc 지역구, 가나다 지역구)로 총 두번만 실행된다.
    Mockito
        .verify(houseDealAPIHandler, Mockito.times(2))
        .getMonthlyAreaDealInfo(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
  }
}
