package com.ssafy.happyhouse.model;

import com.ssafy.happyhouse.dto.response.user.UserTokenDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberDto : 회원정보", description = "회원의 상세 정보를 나타낸다.")
public class UserDto {

	@ApiModelProperty(value = "회원 인덱스")
	private int uId;
	@ApiModelProperty(value = "회원 아이디")
	private String userId;
	@ApiModelProperty(value = "회원 비밀번호")
	private String userPwd;
	@ApiModelProperty(value = "회원 이름")
	private String userName;
	@ApiModelProperty(value = "회원 주소")
	private String userAddr;
	@ApiModelProperty(value = "회원 전화번호")
	private String userTel;
	@ApiModelProperty(value = "회원 가입일")
	private String joinDate;
	
	public int getUId() {
		return uId;
	}
	
	public void setUId(int uId) {
		this.uId = uId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserPwd() {
		return userPwd;
	}
	
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserAddr() {
		return userAddr;
	}
	
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}
	
	public String getUserTel() {
		return userTel;
	}
	
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	
	public String getJoinDate() {
		return joinDate;
	}
	
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
	@Override
	public String toString() {
		return "UserDto [uId=" + uId + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", userAddr=" + userAddr + ", userTel=" + userTel + ", joinDate=" + joinDate + "]";
	}
	
}
