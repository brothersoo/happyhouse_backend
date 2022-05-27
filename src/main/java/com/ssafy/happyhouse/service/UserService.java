package com.ssafy.happyhouse.service;

import com.ssafy.happyhouse.dto.request.user.LoginDto;
import com.ssafy.happyhouse.dto.request.user.RegisterDto;
import com.ssafy.happyhouse.dto.request.user.UpdateDto;
import com.ssafy.happyhouse.dto.response.user.UserTokenDto;
import java.util.List;
import java.util.Map;

import com.ssafy.happyhouse.model.UserDto;

public interface UserService {

//	UserTokenDto login(LoginDto loginDto) throws Exception;
	UserDto login(UserDto userDto) throws Exception;
	UserDto userInfo(String userId) throws Exception;


	int idCheck(String userId) throws Exception;
	void registerUser(RegisterDto registerDto) throws Exception;		// 회원등록
	
	List<UserDto> listUser() throws Exception;
	UserDto getUser(String userId) throws Exception;				// 회원조회
	void updateUser(UpdateDto updateDto) throws Exception;			// 회원수정
	void deleteUser(String userId) throws Exception;		// 회원탈퇴
}
