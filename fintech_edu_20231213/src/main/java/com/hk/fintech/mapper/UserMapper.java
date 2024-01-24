package com.hk.fintech.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hk.fintech.dtos.AuthTokenDto;
import com.hk.fintech.dtos.UserDto;

@Mapper
public interface UserMapper {

	//회원가입(회원정보)
	public boolean addUser(UserDto dto);
	//회원가입(회원토큰정보)
	public boolean addUserToken(AuthTokenDto dto);
	//회원로그인
	public UserDto loginUser(UserDto dto);
	//토큰가져오기
	public AuthTokenDto getToken(int user_seq);
	//card토큰가져오기
	public AuthTokenDto getCardToken(int user_seq);
}





