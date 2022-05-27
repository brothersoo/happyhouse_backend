package com.ssafy.happyhouse.repository;

import com.ssafy.happyhouse.dto.request.user.LoginDto;
import com.ssafy.happyhouse.dto.request.user.RegisterDto;
import com.ssafy.happyhouse.dto.request.user.UpdateDto;
import com.ssafy.happyhouse.dto.response.user.UserTokenDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.UserDto;

@Mapper
public interface UserMapper {

	UserTokenDto login(LoginDto loginDto) throws Exception;
	UserDto login(UserDto userDto) throws SQLException;
	UserDto userInfo(String userId) throws SQLException;

	
	int idCheck(String checkId) throws Exception;
	int registerUser(RegisterDto registerDto) throws Exception;
	
	List<UserDto> listUser() throws Exception;	
	UserDto getUser(String userId)throws Exception;
	void updateUser(UpdateDto updateDto)throws Exception;
	int deleteUser(String userId)throws Exception;
	
}
