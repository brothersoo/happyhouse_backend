package com.ssafy.happyhouse.model;

public class UserDto {

	private int uId;
	private String userId;
	private String userPwd;
	private String userName;
	private String userAddr;
	private String userTel;
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
