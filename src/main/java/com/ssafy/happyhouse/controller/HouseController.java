package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.dto.request.DealUpdateDto;
import com.ssafy.happyhouse.dto.response.DateRange;
import com.ssafy.happyhouse.dto.response.AverageDealsInRange;
import com.ssafy.happyhouse.service.housedeal.HouseDealFacadeService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/house")
@RestController
public class HouseController {

  private final HouseDealFacadeService houseService;

  @GetMapping
  @ApiOperation("")
  public ResponseEntity<?> getHousesInArea(@RequestParam String code) {
    try {
      List<House> houses = houseService.getHousesInArea(code);
      return new ResponseEntity<>(houses, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @GetMapping("/deal")
  @ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 가져옵니다.", response= HouseDeal.class)
  public ResponseEntity<?> getDealsInAreaYearMonth(@RequestParam String code,
      @RequestParam int year, @RequestParam int month) {
    try {
      List<HouseDeal> deals = houseService.getDealsByCodeDate(code, year, month);
      return new ResponseEntity<>(deals, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @GetMapping("/deal_graph")
  @ApiOperation(value="")
  public ResponseEntity<?> getHouseAverageDealsInRangeForGraph(
      @RequestParam String code, @RequestParam Long houseId, @RequestParam String type,
      @RequestParam Integer fromYear, @RequestParam Integer toYear,
      @RequestParam Optional<Integer> fromMonth, @RequestParam Optional<Integer> toMonth) {
    try {
      AverageDealsInRange averageDealsInRange
          = houseService.getDealsByCodeAndDateRange(code, houseId,
          new DateRange(type, fromYear, toYear, fromMonth.get(), toMonth.get()));
      return new ResponseEntity<>(averageDealsInRange, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @PutMapping("/deal/update")
  @ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 데이터베이스에 갱신합니다.", response= Sido.class)
  public ResponseEntity<?> updateDealsInAreaYearMonth(@RequestBody DealUpdateDto dealUpdateDto) {
    try {
      int[] updatedNum = houseService.updateDeal(dealUpdateDto);
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
