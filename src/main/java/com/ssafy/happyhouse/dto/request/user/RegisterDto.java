package com.ssafy.happyhouse.dto.request.user;

import lombok.Data;

@Data
public class RegisterDto {

  private String userId;
  private String userPwd;
  private String userName;
  private String userAddr;
  private String userTel;
}
