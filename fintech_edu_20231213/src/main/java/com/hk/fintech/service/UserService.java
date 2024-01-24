package com.hk.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.fintech.apidto.TokenResponseDto;
import com.hk.fintech.command.AddUserCommand;
import com.hk.fintech.command.LoginCommand;
import com.hk.fintech.dtos.AuthTokenDto;
import com.hk.fintech.dtos.UserDto;
import com.hk.fintech.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	//회원가입
	@Transactional
	public boolean addUser(AddUserCommand command) {
		System.out.println(command);
		//회원테이블(name,email,password,userseqno)
		UserDto uDto=new UserDto();
		uDto.setUsername(command.getUsername());//name
		uDto.setUseremail(command.getUseremail());//email
		uDto.setUserpassword(command.getUserpassword());//password
		uDto.setUserseqno(Integer.parseInt(command.getUserseqno()));//userseqno
		userMapper.addUser(uDto);//회원정보추가
		
		//토큰테이블(토큰,회원번호,Scope)
		AuthTokenDto aDto=new AuthTokenDto();
		aDto.setAccesstoken(command.getUseraccesstoken());//token
		aDto.setScope(command.getUserscope());//scope
		aDto.setUser_seq(uDto.getUser_seq());//회원번호(FK)
		return userMapper.addUserToken(aDto);//토큰정보추가
		
	}
	//회원로그인
	public String loginUser(LoginCommand command
			                ,HttpServletRequest request) {
		UserDto uDto=new UserDto();
		uDto.setUseremail(command.getEmail());
		uDto.setUserpassword(command.getPassword());
		UserDto ldto= userMapper.loginUser(uDto);
		String path="";
		if(ldto==null) {
			path="/";
		}else {
			System.out.println("로그인성공");
			HttpSession session=request.getSession();
			session.setAttribute("ldto", ldto);
			session.setMaxInactiveInterval(60*10);//10분
			path="/main";
		}
		return path;
	}
	
	//토큰 저장하기
	public boolean addToken(TokenResponseDto tokenResponseDto, int user_seq) {
		AuthTokenDto adto=new AuthTokenDto();
		adto.setUser_seq(user_seq);
		adto.setAccesstoken(tokenResponseDto.getAccess_token());
		adto.setScope(tokenResponseDto.getScope());
		
		return userMapper.addUserToken(adto);
	}
	
	//card토큰 가져오기
	public AuthTokenDto getCardToken(int user_seq) {
		return userMapper.getCardToken(user_seq);
	}
	
}
