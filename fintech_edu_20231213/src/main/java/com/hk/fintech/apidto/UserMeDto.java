package com.hk.fintech.apidto;

import java.util.List;

import lombok.Data;

@Data
public class UserMeDto {

	private String api_tran_id;
	private String api_tran_dtm;
	private String rsp_code;
	private String rsp_message;
	private String user_seq_no;
	private String user_ci;
	private String user_name;
	private String user_info;
	private String user_gender;
	private String user_cell_no;
	private String user_email;
	private String res_cnt;
	private List<UserMeAccountDto> res_list;
	private String inquiry_card_cnt;
	private List<UserMeCardDto> inquiry_card_list;
	private String inquiry_pay_cnt;
	private List<UserMePayDto> inquiry_pay_list;
	private String inquiry_insurance_cnt;
	private List<UserMeInsuranceDto> inquiry_insurance_list;
	private String inquiry_loan_cnt;
	private List<UserMeLoanDto> inquiry_loan_list;
}
