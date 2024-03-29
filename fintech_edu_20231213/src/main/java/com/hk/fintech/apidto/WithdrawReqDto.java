package com.hk.fintech.apidto;

import org.springframework.web.bind.annotation.RequestBody;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WithdrawReqDto {
	private String bank_tran_id;
	private String cntr_account_type;
	private String cntr_account_num;
	private String dps_print_content;
	private String fintech_use_num;
	private String tran_amt;
	private String tran_dtime;
	private String req_client_name;
	private String req_client_num;
	private String transfer_purpose;
	private String req_client_fintech_use_num;
	private String recv_client_name;
	private String recv_client_bank_code;
	private String recv_client_account_num;
}
