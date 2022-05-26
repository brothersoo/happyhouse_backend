package com.ssafy.happyhouse.dto.request.user;

import lombok.Data;

@Data
public class UpdateDto {

  private String userPwd;
  private String userName;
  private String userAddr;
  private String userTel;
}
