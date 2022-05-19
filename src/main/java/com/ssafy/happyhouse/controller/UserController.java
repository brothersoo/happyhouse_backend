package com.ssafy.happyhouse.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.UserDto;
import com.ssafy.happyhouse.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/user")
@Api(value="사용자 컨트롤러")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@GetMapping("/register")	
	public String register() {
		return "user/register";
	}
	
	@GetMapping("/idcheck")
	public @ResponseBody String idCheck(@RequestParam("ckid") String checkId) throws Exception {
		int idCount = userService.idCheck(checkId);
		JSONObject json = new JSONObject();
		json.put("idcount", idCount);
		return json.toString();
	}
	
	@ApiOperation(value = "회원등록을 처리합니다.", response=UserDto.class)
	@PostMapping("/register")	// 회원가입 요청
	public String register(UserDto userDto, Model model) throws Exception {
		
		logger.debug("userDto info : {}", userDto);
		userService.registerUser(userDto);
		return "redirect:/user/login";
	}
	
	@GetMapping("/update")
	public String userUpdate() {
		return "user/update";
	}
	
	@ApiOperation(value = "로그인한 회원의 정보를 수정합니다.", response=UserDto.class)
	@PostMapping("/update")		// 회원정보 수정
	public String userUpdate(UserDto userDto, HttpSession session) throws Exception {
//		UserDto userDto = (UserDto) session.getAttribute("userinfo");
		
		if(userDto != null) {
			userService.updateUser(userDto);
			
			return "user/list";
		} else {
			return "redirect:/user/login";
		}
	}
	
	@ApiOperation(value = "회원정보를 삭제합니다.", response=UserDto.class)
	@GetMapping("/delete")
	public String userDelete(HttpSession session) throws Exception {
		UserDto userDto = (UserDto) session.getAttribute("userinfo");
		userService.deleteUser(userDto.getUserId());
		session.invalidate();
		return "redirect:/";
	}

	
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}

	@ApiOperation(value = "로그인 처리합니다.", response=UserDto.class)
	@PostMapping("/login")	// 로그인 요청
	public String login(@RequestParam Map<String, String> map, Model model, HttpSession session,
			HttpServletResponse response) throws Exception {
		logger.debug("map : {}", map.get("userId"));
		UserDto userDto = userService.login(map);
		if (userDto != null) {	// 로그인 성공
			session.setAttribute("userinfo", userDto);

			Cookie cookie = new Cookie("login_id", map.get("userId"));
			cookie.setPath("/");
			if ("saveok".equals(map.get("idsave"))) {	// 아이디 저장 체크
				cookie.setMaxAge(60 * 60 * 24 * 365 * 40);
			} else {
				cookie.setMaxAge(0);
			}
			response.addCookie(cookie);
			return "redirect:/";
		} else {	// 로그인 실패
			model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
			return "user/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@ApiOperation(value = "로그인한 회원의 정보를 반환합니다.", response=UserDto.class)
	@GetMapping("/showInfo")
	public String list(HttpSession session) throws Exception {
		UserDto user = (UserDto) session.getAttribute("userinfo");
		UserDto userDto = userService.getUser(user.getUserId());
		session.setAttribute("user", userDto);
		return "user/showInfo";
	}
	
	@GetMapping("/list")	// 회원정보리스트(관리자모드)
	public String list() {
		return "user/list";
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
