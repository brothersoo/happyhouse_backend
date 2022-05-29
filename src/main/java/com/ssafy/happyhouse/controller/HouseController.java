package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.service.house.HouseService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/house")
@RestController
public class HouseController {

  private final HouseService houseService;

  @GetMapping("/upmyundong/{upmyundongId}")
  @ApiOperation(value="읍면동 내의 아파트 정보를 전달합니다.", response=List.class)
  public ResponseEntity<?> getHousesInArea(@PathVariable Long upmyundongId) {
    try {
      List<House> houses = houseService.getHousesInUpmyundong(upmyundongId);
      return new ResponseEntity<>(houses, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  private ResponseEntity<String> exceptionHandling(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
