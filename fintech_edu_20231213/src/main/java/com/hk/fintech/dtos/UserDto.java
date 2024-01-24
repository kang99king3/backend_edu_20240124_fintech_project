package com.hk.fintech.dtos;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("UserDto")
@Data
public class UserDto {
	
	private int user_seq;//회원번호
	private String username;//회원이름
	private String useremail;//회원메일
	private String userpassword;//회원비밀번호
	private int userseqno; // 사용자일련번호
	private List<AuthTokenDto> authtokenlist;//토큰정보
}
