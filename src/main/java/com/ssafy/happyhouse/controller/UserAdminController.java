package com.ssafy.happyhouse.controller;

import com.ssafy.happyhouse.dto.request.user.RegisterDto;
import com.ssafy.happyhouse.dto.request.user.UpdateDto;
import java.util.LinkedList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.UserDto;
import com.ssafy.happyhouse.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequiredArgsConstructor
@Api("어드민 컨트롤러 V1")
@RequestMapping("/admin")
@RestController
public class UserAdminController {

private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

	private final UserService userService;

	@GetMapping(value = "/user")
	public ResponseEntity<?> userList() {
		try {
			List<UserDto> list = userService.listUser();
			if(list != null && !list.isEmpty()) {
				return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
			} else {
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
		
	}
	
	@ApiOperation(value = "회원등록", notes = "회원의 정보를 받아 처리.")
	@PostMapping(value = "/user")
	public ResponseEntity<?> userRegister(@RequestBody RegisterDto registerDto) {
		try {
			userService.registerUser(registerDto);
			List<UserDto> list = userService.listUser();
			return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
		
	}
	
	@ApiOperation(value = "회원정보", notes = "회원한명에 대한 정보.")
	@GetMapping(value = "/user/{userid}")
	public ResponseEntity<?> userInfo(@PathVariable("userid") String userid) {
		try {
			logger.debug("파라미터 : {}", userid);
			UserDto memberDto = userService.getUser(userid);
			if(memberDto != null)
				return new ResponseEntity<UserDto>(memberDto, HttpStatus.OK);
			else
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "회원정보수정", notes = "회원정보를 수정합니다.")
	@PutMapping(value = "/user")
	public ResponseEntity<?> userModify(@RequestBody UpdateDto updateDto) {
		try {
			userService.updateUser(updateDto);
			List<UserDto> list = userService.listUser();
			return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "회원정보삭제", notes = "회원정보를 삭제합니다.")
	@DeleteMapping(value = "/user/{userid}")
	public ResponseEntity<?> userDelete(@PathVariable("userid") String userId) {
		try {
			userService.deleteUser(userId);
			List<UserDto> list = userService.listUser();
			return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

  @ApiOperation(value = "회원정보검색", notes = "회원이름정보를 검색합니다.")
  @GetMapping(value = "/user/search") 
  public ResponseEntity<?> userSearch(String keyword) { 
	  // make Table
	  LinkedList<UserDto> searchlist = new LinkedList<UserDto>();
	  
	  int n2 = keyword.length(); 
	  int[] table = new int[n2];
	  
	  int idx = 0; 
	  for (int i = 1; i < n2; i++) { 
		  while(idx>0 && keyword.charAt(i) != keyword.charAt(idx)) { 
			  idx = table[idx-1]; 
		  }
	  
	  if(keyword.charAt(i) == keyword.charAt(idx)) { 
		  idx++; table[i] = idx; 
		  } 
	  }
	  
	  try { 
		  List<UserDto> list = userService.listUser(); 
		  int size = list.size();
		  String parent;
	  
		  for (int s = 0; s < size; s++) { 
			  parent = list.get(s).getUserName(); 
			  int n1 = parent.length();
		  
			  int j = 0; // 현재 대응되는 글자 수 
			  for (int i = 0; i < n1; i++) { 
				  while(j>0 && parent.charAt(i) != keyword.charAt(j)) { // j번째 글자와 해당 글자가 불일치할 경우 
					  j = table[j-1]; 
				  }
			  
				  if(parent.charAt(i) == keyword.charAt(j)) { // 글자가 대응될 경우 
					  if(j == n2-1) {
						  searchlist.offer(list.get(s)); 
						  j = table[j]; 
					  } else { 
						  j++; 
					  } 
				  } 
			  } 
		  } 
		  return new ResponseEntity<List<UserDto>>(searchlist, HttpStatus.OK);
	  
	  } catch (Exception e) { 
		  return exceptionHandling(e); 
	  }
  
  }
	 

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
