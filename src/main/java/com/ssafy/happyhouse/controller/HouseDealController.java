package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.graph.ChartData;
import com.ssafy.happyhouse.service.facade.HouseDealFacadeService;
import com.ssafy.happyhouse.service.housedeal.HouseDealService;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/house_deal")
@RestController
public class HouseDealController {

  private final HouseDealService houseDealService;
  private final HouseDealFacadeService houseDealFacadeService;

  @GetMapping("/house/{houseId}")
  @ApiOperation(value="해당 아파트의 거래 내역을 가져옵니다.", response=List.class)
  public ResponseEntity<?> getDealsOfHouse(@PathVariable Long houseId) {
    try {
      List<HouseDeal> deals = houseDealService.getDealsOfHouse(houseId);
      return new ResponseEntity<>(deals, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @GetMapping("/graph")
  @ApiOperation(value="주어진 날짜 사이의 아파트 매매 그래프 정보를 가져옵니다.", response = ChartData.class)
  public ResponseEntity<?> getHouseAverageDealsInRangeForGraph(
      @RequestParam List<Long> houseIds, @RequestParam String type,
      @RequestParam Integer fromYear, @RequestParam Integer toYear,
      @RequestParam Optional<Integer> fromMonth, @RequestParam Optional<Integer> toMonth) {
    try {
      ChartData chartData
          = houseDealFacadeService.getChartDataOfHouses(houseIds,
          new DateRange(type, fromYear, toYear, fromMonth.get(), toMonth.get()));
      return new ResponseEntity<>(chartData, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @PutMapping("/update")
  @ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 데이터베이스에 갱신합니다.", response=Array.class)
  public ResponseEntity<?> updateDealsInAreaYearMonth(@RequestBody DealUpdateDto dealUpdateDto) {
    try {
      int[] updatedNum = houseDealFacadeService.updateDeal(dealUpdateDto);
      return new ResponseEntity<>(updatedNum, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  private ResponseEntity<String> exceptionHandling(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
