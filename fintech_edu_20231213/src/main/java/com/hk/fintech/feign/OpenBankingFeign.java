package com.hk.fintech.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.hk.fintech.apidto.AccountBalanceDto;
import com.hk.fintech.apidto.AccountCancelDto;
import com.hk.fintech.apidto.AccountCancelReqDto;
import com.hk.fintech.apidto.AccountListDto;
import com.hk.fintech.apidto.AccountTransactionListDto;
import com.hk.fintech.apidto.CardListDto;
import com.hk.fintech.apidto.TokenResponseDto;
import com.hk.fintech.apidto.TokenResponseOobDto;
import com.hk.fintech.apidto.UserMeDto;
import com.hk.fintech.apidto.DepositReqDto;
import com.hk.fintech.apidto.DepositResDto;
import com.hk.fintech.apidto.TokenRequestOobDto;
import com.hk.fintech.apidto.WithdrawReqDto;
import com.hk.fintech.apidto.WithdrawResDto;

//restAPI 서버에 요청하고 결과값을 받아 줄 feign 인터페이스
@FeignClient(name="feign", url="https://testapi.openbanking.or.kr")
public interface OpenBankingFeign {
	
  //어노테이션에 설정된 경로로 파라미터 값들과 함께 요청	

  //나의 정보 조회
  @GetMapping(path = "/v2.0/user/me")
  public UserMeDto requestUserMe(
          @RequestHeader("Authorization") String access_token,
		  @RequestParam("user_seq_no") String user_seq_no);
	
  //토큰 발급	
  @PostMapping(path = "/oauth/2.0/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  public TokenResponseDto requestToken(
		  @RequestParam("code") String code, 
		  @RequestParam("client_id") String client_id, 
		  @RequestParam("client_secret") String client_secret, 
		  @RequestParam("redirect_uri") String redirect_uri, 
		  @RequestParam("grant_type") String grant_type);
  
  //토큰 발급(oob)	
  @PostMapping(path = "/oauth/2.0/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  public TokenResponseOobDto requestOobToken(
		  @RequestBody TokenRequestOobDto requestOobDto);
  
  //계좌목록조회
  @GetMapping(path = "/v2.0/account/list")
  public AccountListDto requestAccountList(
          @RequestHeader("Authorization") String access_token,
		  @RequestParam("user_seq_no") String user_seq_no, 
		  @RequestParam("include_cancel_yn") String include_cancel_yn, 
		  @RequestParam("sort_order") String sort_order);
  
  //잔액조회
  @GetMapping(path = "/v2.0/account/balance/fin_num")
  public AccountBalanceDto requestAccountBalance(
      @RequestHeader("Authorization") String access_token,
		  @RequestParam("bank_tran_id") String bank_tran_id, 
		  @RequestParam("fintech_use_num") String fintech_use_num, 
		  @RequestParam("tran_dtime") String tran_dtime);

  //거래내역조회
  @GetMapping(path = "/v2.0/account/transaction_list/fin_num")
  public AccountTransactionListDto requestAccountTransactionList(
		  @RequestHeader("Authorization") String access_token,
		  @RequestParam("bank_tran_id") String bank_tran_id, 
		  @RequestParam("fintech_use_num") String fintech_use_num, 
		  @RequestParam("inquiry_type") String inquiry_type, 
		  @RequestParam("inquiry_base") String inquiry_base, 
		  @RequestParam("from_date") String from_date, 
		  @RequestParam("to_date") String to_date, 
		  @RequestParam("sort_order") String sort_order, 
		  @RequestParam("tran_dtime") String tran_dtime);
  
  
  	
  
    //출금이체
  	@PostMapping(path="/v2.0/transfer/withdraw/fin_num",produces = "application/json")
	public WithdrawResDto requestWithdraw(
			@RequestHeader("Authorization") String access_token,
			@RequestBody WithdrawReqDto wrdto);
	
  	//입금이체
	@PostMapping(path="/v2.0/transfer/deposit/fin_num",produces = "application/json")
	public DepositResDto requestDeposit(
			@RequestHeader("Authorization") String access_token,
			@RequestBody DepositReqDto drdto);
	
	//계좌해지
	@PostMapping(path="/v2.0/account/cancel",produces = "application/json")
	public AccountCancelDto requestAccountCancel(
			@RequestHeader("Authorization") String access_token,
			@RequestBody AccountCancelReqDto ardto
			);
	
	//카드목록조회
	@GetMapping(path="/v2.0/cards")
	public CardListDto requestAccountCardList(
			@RequestHeader("Authorization") String access_token,
			@RequestParam("bank_tran_id") String bank_tran_id,
			@RequestParam("user_seq_no") String user_seq_no,
			@RequestParam("bank_code_std") String bank_code_std,
			@RequestParam("member_bank_code") String member_bank_code
			);
}







