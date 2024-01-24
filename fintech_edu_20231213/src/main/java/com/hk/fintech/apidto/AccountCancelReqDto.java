package com.hk.fintech.apidto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountCancelReqDto {
	private final String bank_tran_id;
	private final String scope;
	
	//핀테크이용번호로 해지시 사용
	private final String fintech_use_num;
	
	//계좌번호로 해지시 사용
	private String user_seq_no;
	private String bank_code_std;
	private String account_num;
	private String account_seq;
}
