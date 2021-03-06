package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.service.facade.AreaFacadeServiceImpl;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/area")
@RestController
public class AreaController {

  private final AreaFacadeServiceImpl areaService;

  @GetMapping("/sido")
  @ApiOperation(value="등록된 특별시/도를 반환합니다.", response=Sido.class)
  public ResponseEntity<?> getAllSidoes() {
    try {
      List<Sido> sidoes = areaService.searchAllSidos();
      return new ResponseEntity<List<Sido>>(sidoes, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @GetMapping("/sigugun")
  @ApiOperation(value="특별시/도에 속한 시/구/군을 반환합니다.", response= Sido.class)
  public ResponseEntity<?> getAllSiguguns(@RequestParam String sidoCode) {
    try {
      List<Sigugun> siguguns = areaService.searchSigugunsInSido(sidoCode);
      return new ResponseEntity<List<Sigugun>>(siguguns, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  @GetMapping("/upmyundong")
  @ApiOperation(value="시/구/군에 속한 읍/면/동을 반환합니다.", response=Sido.class)
  public ResponseEntity<?> getAllUpmyundongs(@RequestParam String sigugunCode) {
    try {
      List<Upmyundong> upmyundongs= areaService.searchUpmyundongsInSigugun(sigugunCode);
      return new ResponseEntity<List<Upmyundong>>(upmyundongs, HttpStatus.OK);
    } catch (Exception e) {
      return exceptionHandling(e);
    }
  }

  private ResponseEntity<String> exceptionHandling(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
