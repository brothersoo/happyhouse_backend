package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.dto.request.user.LoginDto;
import com.ssafy.happyhouse.dto.request.user.RegisterDto;
import com.ssafy.happyhouse.dto.request.user.UpdateDto;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.happyhouse.model.UserDto;
import com.ssafy.happyhouse.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RequiredArgsConstructor
@Api(value="사용자 컨트롤러")
@RequestMapping("/user")
@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	@ApiOperation(value = "회원등록을 처리합니다.", response=UserDto.class)
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
		try {
			userService.registerUser(registerDto);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "로그인 처리합니다.", response=UserDto.class)
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		try {
			return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "회원 정보를 반환합니다.", response=UserDto.class)
	@GetMapping("/user/{userid}")
	public ResponseEntity<?> retrieve(@PathVariable String userid) {
		try {
			return new ResponseEntity<>(userService.getUser(userid), HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "로그인한 회원의 정보를 수정합니다.", response=UserDto.class)
	@PutMapping("update")
	public ResponseEntity<?> update(@RequestBody UpdateDto updateDto) {
		try {
			userService.updateUser(updateDto);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "회원정보를 삭제합니다.", response=UserDto.class)
	@DeleteMapping("/user/{userid}")
	public ResponseEntity<?> withdrawal(@PathVariable("userid") String userId) {
		try {
			userService.deleteUser(userId);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
