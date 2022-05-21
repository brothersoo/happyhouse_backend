package com.ssafy.happyhouse.service;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.UserDto;
import com.ssafy.happyhouse.repository.UserMapper;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	@Override
	public int idCheck(String checkId) throws Exception{
		return userMapper.idCheck(checkId);	// 0 or 1
	}

	@Override
	public void registerUser(UserDto userDto) throws Exception {
		userMapper.registerUser(userDto);
	}

	@Override
	public UserDto login(Map<String, String> map) throws Exception {
		return userMapper.login(map);
	}

	@Override
	public List<UserDto> listUser() throws Exception {
		return userMapper.listUser();
	}

	@Override
	public UserDto getUser(String userId) throws Exception {
		return userMapper.getUser(userId);
	}

	@Override
	public void updateUser(UserDto userDto) throws Exception {
		userMapper.updateUser(userDto);
	}

	@Override
	public void deleteUser(String userId) throws Exception {
		userMapper.deleteUser(userId);
	}

	
}
