package com.ssafy.happyhouse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.area.Sido;
import com.ssafy.happyhouse.model.house.DealInfo;
import com.ssafy.happyhouse.model.house.HouseDeal;
import com.ssafy.happyhouse.service.HouseService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/house")
@CrossOrigin(origins = "/*", methods = {RequestMethod.GET , RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class HouseController {

	private HouseService houseService;
	
	public HouseController(HouseService houseService) {
		this.houseService = houseService;
	}

	@GetMapping("/deal")
	@ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 가져옵니다.", response=DealInfo.class)
	public ResponseEntity<?> getDealsInAreaYearMonth(@RequestParam String code,
			@RequestParam int year, @RequestParam int month, @RequestParam(value="standard", required=false, defaultValue="standard") String standard) {
		try {
			List<DealInfo> deals = houseService.getDealsByCodeDate(code, year, month, standard);
			return new ResponseEntity<List<DealInfo>>(deals, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@PutMapping("/deal/update")
	@ApiOperation(value="지역 코드의 년/월에 발생한 거래 내역을 데이터베이스에 갱신합니다.", response=Integer.class)
	public ResponseEntity<?> updateDealsInAreaYearMonth(@RequestParam String code,
			@RequestParam int year, @RequestParam int month) {
		try {
			int updatedNum = houseService.updateDeal(code, year, month);
			return new ResponseEntity<Integer>(updatedNum, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
