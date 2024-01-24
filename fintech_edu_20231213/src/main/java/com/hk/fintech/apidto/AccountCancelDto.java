package com.hk.fintech.apidto;

import lombok.Data;
import lombok.ToString;

//계좌 해지 응답정보
@Data
@ToString
public class AccountCancelDto {
	private String api_tran_id;
	private String api_tran_dtm;
	private String rsp_code;
	private String rsp_message;
	private String bank_tran_id;
	private String bank_tran_date;
	private String bank_code_tran;
	private String bank_rsp_code;
	private String bank_rsp_message;
}
