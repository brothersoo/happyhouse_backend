package com.ssafy.happyhouse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.area.Sido;
import com.ssafy.happyhouse.model.area.Sigugun;
import com.ssafy.happyhouse.model.area.Upmyundong;
import com.ssafy.happyhouse.repository.AreaRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/area")
@CrossOrigin(origins = "/*", methods = {RequestMethod.GET , RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AreaController {

	private AreaRepository areaRepository;
	
	public AreaController(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}
	
	@GetMapping("/sido")
	@ApiOperation(value="등록된 특별시/도를 반환합니다.", response=Sido.class)
	public ResponseEntity<?> getAllSidoes() {
		try {
			List<Sido> sidoes = areaRepository.findAllSidoes();
			return new ResponseEntity<List<Sido>>(sidoes, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping("/sigugun")
	@ApiOperation(value="특별시/도에 속한 시/구/군을 반환합니다.", response=Sido.class)
	public ResponseEntity<?> getAllSiguguns(@RequestParam int sidoId) {
		try {
			List<Sigugun> siguguns = areaRepository.findAllSiguguns(sidoId);
			return new ResponseEntity<List<Sigugun>>(siguguns, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping("/upmyundong")
	@ApiOperation(value="시/구/군에 속한 읍/면/동을 반환합니다.", response=Sido.class)
	public ResponseEntity<?> getAllUpmyundongs(@RequestParam int sigugunId) {
		try {
			List<Upmyundong> upmyundongs= areaRepository.findAllUpmyundongs(sigugunId);
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
