package com.ssafy.happyhouse.service;

import java.util.List;
import java.util.Map;

import com.ssafy.happyhouse.model.UserDto;

public interface UserService {
	
	UserDto login(Map<String, String> map) throws Exception;

	int idCheck(String userId) throws Exception;
	void registerUser(UserDto userDto) throws Exception;		// 회원등록
	
	List<UserDto> listUser() throws Exception;
	UserDto getUser(String userId) throws Exception;				// 회원조회
	void updateUser(UserDto userDto) throws Exception;			// 회원수정
	void deleteUser(String userId) throws Exception;		// 회원탈퇴
}
