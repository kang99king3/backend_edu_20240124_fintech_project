package com.hk.fintech.dtos;

import groovy.transform.ToString;
import lombok.Data;

@Data
@ToString
public class Account_infoDto {

	private int account_info_seq;
	private int user_seq;
	private String fintech_use_num;
	private long balance_amt;
	private String bank_name;
	private String account_num_masked;
	private String rep_account;
}
