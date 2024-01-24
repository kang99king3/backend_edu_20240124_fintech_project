package com.hk.fintech.apidto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CardListDto {

	private String api_tran_id;             //거래고유번호(API)
	private String api_tran_dtm;            //거래일시(밀리세컨드)
	private String rsp_code;                //응답코드(API)
	private String rsp_message;             //응답메시지(API)
	private String bank_tran_id;            //거래고유번호(참가기관)
	private String bank_tran_date;          //거래일자(참가기관)
	private String bank_code_tran;          //응답코드를 부여한 참가기관,표준(대표)코드
	private String bank_rsp_code;           //응답코드(참가기관)
	private String bank_rsp_message;        //응답메시지(참가기관)
	private String user_seq_no;             //사용자일련번호
	private String next_page_yn;            //다음페이지 존재여부("Y":다음 페이지 존재,"N":마지막 페이지)
	private String befor_inquiry_trace_info;//직전조회추적정보
	private String card_cnt;                //현재 페이지 조회 건수
	private List<CardDto> card_list;        //카드목록
}
