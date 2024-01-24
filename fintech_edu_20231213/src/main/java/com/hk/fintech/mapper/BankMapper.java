package com.hk.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hk.fintech.dtos.Account_infoDto;

@Mapper
public interface BankMapper {

	//나의계좌 연결(계좌를 DB에 등록)
	public boolean addUserAccount(Account_infoDto dto);
	//연결된 계좌목록 가져오기(DB에 저장된 계좌정보)
	public List<Account_infoDto> myAccount(int user_seq);
	//나의 계좌 존재여부 확인하기(fintech_use_num이용)
	public Account_infoDto myAccountSearch(String fintech_use_num);
	//나의 대표계좌 가져오기
	public Account_infoDto repAccount(int user_seq);
}
