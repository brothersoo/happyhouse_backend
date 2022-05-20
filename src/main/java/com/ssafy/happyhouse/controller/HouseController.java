package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.service.housedeal.HouseDealFacadeService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/house")
@CrossOrigin(origins = "/*", methods = {RequestMethod.GET , RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
public class HouseController {

  private HouseDealFacadeService houseService;

  public HouseController(HouseDealFacadeService houseService) {
    this.houseService = houseService;
  }

  @GetMapping("/deal")
  @ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 가져옵니다.", response= DealInfo.class)
  public ResponseEntity<?> getDealsInAreaYearMonth(@RequestParam String code,
      @RequestParam int year, @RequestParam int month) {
    try {
      List<DealInfo> deals = houseService.getDealsByCodeDate(code, year, month);
      return new ResponseEntity<List<DealInfo>>(deals, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @PutMapping("/deal/update")
  @ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 데이터베이스에 갱신합니다.", response= Sido.class)
  public ResponseEntity<?> updateDealsInAreaYearMonth(@RequestParam String code,
      @RequestParam int year, @RequestParam int month, @RequestParam long umdId) {
    try {
      int[] updatedNum = houseService.updateDeal(code, year, month, umdId);
      return new ResponseEntity<int[]>(updatedNum, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  private ResponseEntity<String> exceptionHandling(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
