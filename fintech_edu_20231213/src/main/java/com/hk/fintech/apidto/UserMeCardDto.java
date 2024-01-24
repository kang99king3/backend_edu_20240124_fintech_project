package com.hk.fintech.apidto;

import lombok.Data;

@Data
public class UserMeCardDto {
	
	private String bank_code_std;
	private String member_bank_code;
	private String inquiry_agree_dtime;
	
}
