package com.hk.fintech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.fintech.apidto.AccountDto;
import com.hk.fintech.dtos.Account_infoDto;
import com.hk.fintech.dtos.AuthTokenDto;
import com.hk.fintech.dtos.UserDto;
import com.hk.fintech.mapper.BankMapper;
import com.hk.fintech.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class BankService {
	
	@Autowired
	private BankMapper bankMapper;
	@Autowired
	private UserMapper userMapper;
	
	public boolean addUserAccount(AccountDto adto,int user_seq) {
		//DB에 저장할 계좌 정보만 복사
		//AccountDto --> Account_infoDto로 데이터 복사
		Account_infoDto dto=new Account_infoDto();
		dto.setUser_seq(user_seq);//회원번호
		dto.setFintech_use_num(adto.getFintech_use_num());//핀테크이용번호
		dto.setBalance_amt(Long.parseLong(adto.getBalance_amt()));//계좌잔액
		dto.setBank_name(adto.getBank_name());//계좌 은행이름
		dto.setAccount_num_masked(adto.getAccount_num_masked());//계좌번호
		
		System.out.println(dto);
		
		return bankMapper.addUserAccount(dto);
	}
	
	//나의 계좌목록 가져오기(DB에서)
	public List<Account_infoDto> myAccount(int user_seq){
		return bankMapper.myAccount(user_seq);
	}
	//나의 계좌 조회하기
	public Account_infoDto myAccountSearch(String fintech_use_num) {
		return bankMapper.myAccountSearch(fintech_use_num);
	}
	
	//토큰oob 추가하기
	public boolean addUserToken(AuthTokenDto dto) {
		return userMapper.addUserToken(dto);
	}
	//토큰oob 가져오기(한번만 발급받기위해 발급받았는지 조회하기)
	public AuthTokenDto getToken(int user_seq) {
		return userMapper.getToken(user_seq);
	}
	//나의 대표계좌가져오기
	public Account_infoDto repAccount(int user_seq) {
		return bankMapper.repAccount(user_seq);
	}
	
}




