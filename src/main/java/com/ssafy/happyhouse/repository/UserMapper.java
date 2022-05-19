package com.ssafy.happyhouse.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.UserDto;

@Mapper
public interface UserMapper {
	
	UserDto login(Map<String, String> map) throws Exception;
	
	int idCheck(String checkId) throws Exception;
	int registerUser(UserDto userDto) throws Exception;
	
	List<UserDto> listUser() throws Exception;	
	UserDto getUser(String userId)throws Exception;
	void updateUser(UserDto userDto)throws Exception;	
	int deleteUser(String userId)throws Exception;
	
}
