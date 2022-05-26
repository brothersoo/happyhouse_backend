package com.ssafy.happyhouse.service;

import com.ssafy.happyhouse.dto.request.user.LoginDto;
import com.ssafy.happyhouse.dto.request.user.RegisterDto;
import com.ssafy.happyhouse.dto.request.user.UpdateDto;
import com.ssafy.happyhouse.dto.response.user.UserTokenDto;
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
	public void registerUser(RegisterDto registerDto) throws Exception {
		userMapper.registerUser(registerDto);
	}

	@Override
	public UserTokenDto login(LoginDto loginDto) throws Exception {
		return userMapper.login(loginDto);
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
	public void updateUser(UpdateDto updateDto) throws Exception {
		userMapper.updateUser(updateDto);
	}

	@Override
	public void deleteUser(String userId) throws Exception {
		userMapper.deleteUser(userId);
	}
}
