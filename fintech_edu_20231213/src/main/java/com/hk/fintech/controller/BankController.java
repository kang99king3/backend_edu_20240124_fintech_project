package com.hk.fintech.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.fintech.apidto.AccountBalanceDto;
import com.hk.fintech.apidto.AccountDto;
import com.hk.fintech.apidto.AccountListDto;
import com.hk.fintech.apidto.AccountTransactionListDto;
import com.hk.fintech.apidto.DepositReqDto;
import com.hk.fintech.apidto.DepositReqListDto;
import com.hk.fintech.apidto.DepositResDto;
import com.hk.fintech.apidto.TokenRequestOobDto;
import com.hk.fintech.apidto.TokenResponseOobDto;
import com.hk.fintech.apidto.WithdrawReqDto;
import com.hk.fintech.apidto.WithdrawResDto;
import com.hk.fintech.dtos.Account_infoDto;
import com.hk.fintech.dtos.AuthTokenDto;
import com.hk.fintech.dtos.UserDto;
import com.hk.fintech.feign.OpenBankingFeign;
import com.hk.fintech.service.BankService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/banking")
public class BankController {

	Logger logger=LoggerFactory.getLogger(UserController.class);
	    
    @Autowired
    private OpenBankingFeign openBankingFeign;
    @Autowired
    private BankService bankService;
    
    //연결한 계좌 목록 조회 
    //DB에 계좌 정보를 잔액과 같이 보여주고 거래내역 기능을 연결하기
	@GetMapping("/myaccount")
	public String myAccount(HttpServletRequest request,Model model) {
		logger.info("연결된 나의 계좌목록");
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		
//		AccountListDto accountListDto= openBankingFeign.requestAccountList(
//				"bearer "+ldto.getAuthtokendto().getAccesstoken(),
//				ldto.getUserseqno()+"", "Y", "D");
//		
//		model.addAttribute("res_list", accountListDto.getRes_list());
		
		List<Account_infoDto> alist= bankService.myAccount(ldto.getUser_seq());
		model.addAttribute("alist", alist);
		System.out.println("계좌개수:"+alist.size());
		return "user/myaccount";
	}
	
	//계좌 연결하기(계좌목록 조회결과에서 DB에 등록하고자하는 계좌를 찾아 추가해줌)
	//나의 계좌목록 조회 -> 연결할 계좌 찾기 -> 연결할 계좌의 잔액 조회
	//-> 연결할 계좌의 정보와 잔액을 AccountDto에 저장하고, 회원번호와 함께 서비스로 보내서 DB에 등록한다.
	@ResponseBody
	@GetMapping("/accountconnection")
	public String accountConnection(HttpServletRequest request,
			Model model, String fintech_use_num) {
		System.out.println("계좌연결하기");
		//계좌목록 조회에 필요한 파라미터 값들
		System.out.println("핀테크이용번호:"+fintech_use_num);
		String user_seq_no="";
		String include_cancel_yn="Y";
		String sort_order="D";
		
		//session에서 user_seq_no와 user_seq 값을 가져온다.
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		user_seq_no=ldto.getUserseqno()+"";
		int user_seq=ldto.getUser_seq();
		
		//oob권한 토큰 발급받았는지 확인하기
		AuthTokenDto authTokenDto = bankService.getToken(user_seq);	
		System.out.println(authTokenDto);
		if(authTokenDto==null) {
			logger.info("oob 토큰발급받기");
			//oob 권한 토큰 발급받기
			TokenResponseOobDto responseOobDto=
					openBankingFeign.requestOobToken(new TokenRequestOobDto("4987e938-f84b-4e23-b0a2-3b15b00f4ffd",
							"3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec",
							"oob","client_credentials"));
			//Auth_token테이블에 추가하기
			bankService.addUserToken(new AuthTokenDto(0,
					user_seq,
					responseOobDto.getAccess_token(),
					responseOobDto.getScope()));			
		}
		
		//--------------------DB에 등록된 계좌가 없다면 연결하기 코드 추가
		Account_infoDto account_infoDto = bankService.myAccountSearch(fintech_use_num);
		System.out.println(account_infoDto);
		String scriptPrint="";//계좌 연결 결과를 알려줄 자바스크립트 코드 
		if(account_infoDto==null) {
			//계좌목록가져오기(전달 파라미터:token,user_seq_no,include_cancel_yn,sort_order)
			AccountListDto accountListDto=
					openBankingFeign.requestAccountList("Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken(),				                              
							user_seq_no,include_cancel_yn,sort_order);
			System.out.println(accountListDto);
			
			//DB에 추가할 계좌정보를 찾는다(클라이언트에서 전달된 fintech_use_num와 계좌목록의 fintech_use_num와
			//일치하는 계좌 찾기
			AccountDto adto=null;
			for (AccountDto dto : accountListDto.getRes_list()) {
				if(dto.getFintech_use_num().equals(fintech_use_num)) {
					adto=dto;
				}
			}
			//찾은 계좌 출력해보기
			System.out.println(adto);
			
			//잔액조회하기(전달파라미터:token,bank_tran_id,fintech_use_num,tran_dtime)
			AccountBalanceDto accountBalanceDto=openBankingFeign.requestAccountBalance(
					"Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken(), 
					"M202201886U"+createNum(), 
					fintech_use_num, 
					getDateTime());
			
			System.out.println("잔액:"+accountBalanceDto.getBalance_amt());
			
			//잔액조회에서 구한 잔액을 계좌정보(adto)에 추가한다.
			adto.setBalance_amt(accountBalanceDto.getBalance_amt());
			
			//찾은 계좌정보를 서비스로 전달한다.
			bankService.addUserAccount(adto,user_seq);
			//--------------------
			scriptPrint="<script type='text/javascript'>"
					+"  alert('계좌 연결을 완료하였습니다.');"
					+"  location.href='/user/myinfo';"
					+"</script>";
		}else {
			scriptPrint="<script type='text/javascript'>"
					+"  alert('계좌가 이미 연결되어 있습니다.');"
					+"  location.href='/user/myinfo';"
					+"</script>";
		}
		
		return scriptPrint;
	}
	
	@ResponseBody
	@PostMapping("/transactionlist")
	public Map<String, AccountTransactionListDto> transactionList(
			String fintech_use_num,HttpServletRequest request){
		logger.info("거래내역 조회");
		Map<String, AccountTransactionListDto> map = new HashMap<>();
		
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		
		AccountTransactionListDto accountTransactionListDto=
		openBankingFeign.requestAccountTransactionList(
				"Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken(),
						"M202201886U"+createNum(), 
						fintech_use_num, 
						"A", 
						"D",
						"20230101", 
						"20231231", 
						"D", 
						getDateTime());
		
		map.put("transactionList", accountTransactionListDto);
		
		return map;
	}
	
	@GetMapping("/mytransfer")
	public String myTransfer() {
		logger.info("송금하기");
		return "user/transfer";
	}
	
	//송금하기: A고객 -> B고객  ,진행절차: [A계좌] --출금--> [이용기관(출금계좌)]
	//                                              [이용기관(입금계좌)] --입금--> [B계좌]
	@PostMapping("/withdraw_deposit")
	public String withdraw_deposit(@RequestParam Map<String, String> paramMap,
									HttpServletRequest request, Model model) {
		String cntr_account_num_widthDraw="100000000001";//출금 약정 계좌
		String cntr_account_num_deposit="200000000001";//입금 약정 계좌
		String dps_name=paramMap.get("dps_name");//받는사람 이름	
		String dps_bank=paramMap.get("dps_bank");//받는사람 은행이름
		String dps_fintech_use_num=paramMap.get("dps_fintech_use_num");//받는사람 핀테크이용번호
		String dps_tran_amt=paramMap.get("dps_tran_amt");//보낼 금액
		
		
		//로그인 정보구하기
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		
		//대표계좌구하기
		Account_infoDto account_infoDto = bankService.repAccount(ldto.getUser_seq());
		
		
		//출금이체 요청 ( A계좌에서 이용기관 출금약정계좌로 출금이체)
		WithdrawResDto withdrawResDto=
		openBankingFeign.requestWithdraw("Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken()
				  						,new WithdrawReqDto(
				  								"M202201886U"+createNum(),  //거래고유번호
				  								"N",                        //약정계좌:N, 계정:C
				  								cntr_account_num_widthDraw, //출금약정계좌
				  								"인터넷사용료(입금)",           //입금계좌인자내역
				  								account_infoDto.getFintech_use_num(), //출금계좌핀테크이용번호
				  								dps_tran_amt,               //거래금액
				  								getDateTime(),              //요청일시
				  								ldto.getUsername(),         //요청고객성명
				  								"KANGBYUNGJIN1234",         //요청고객회원번호
				  								"TR",                       //이체용도코드
				  								account_infoDto.getFintech_use_num() , //요청고객핀테크이용번호
				  								 dps_name,                    //최종수취고객명
				  								 "020",                     //최종수취고객계좌 기관코드
				  								 "99"                       //최종수취고객계좌번호(오픈뱅킹센터에서 검증하지 않으므로 임의의 숫자넣었음)
				  								)
				  						); 
		
		//입금이체요청(이용기관 입금약정계좌에서 B계좌로 입금이체)
		List<DepositReqListDto> dpsList=new ArrayList<DepositReqListDto>();
		dpsList.add(new DepositReqListDto(
					"1",                        //거래순번
					"M202201886U"+createNum(),  //거래고유번호
					dps_fintech_use_num, //입금계좌핀테크이용번호
					"이용료(강병진)",              //입금계좌인자내역
					dps_tran_amt,                    //거래금액
					account_infoDto.getFintech_use_num(),	//요청고객핀테크이용번호
					ldto.getUsername() ,                    //요청고객성명
					"KANGBYUNGJIN1234",         //요청고객회원번호
					"TR"						//이체용도
				));
		
		DepositResDto depositResDto=
		openBankingFeign.requestDeposit("Bearer "+ldto.getAuthtokenlist().get(1).getAccesstoken(),
										new DepositReqDto(
												"N",                     //약정계좌:N, 계정:C
												cntr_account_num_deposit,//입금약정계좌번호
												"NONE", 				 //입금이체용 암호문구
												"인터넷사용료",             //출금계좌인자내역
												"off",                   //수취인성명 검증 여부(on:검증,off:미검증)
												getDateTime(),           //요청일시
												"1",                     //입금요청건수
												dpsList
												)
										);
		
		System.out.println(withdrawResDto);
		System.out.println(depositResDto);
		
		model.addAttribute("depositResDto", depositResDto);
		
		return "user/transfer_complete";
	}
	
	@GetMapping("/transfer_comp")
	public String transferComp() {
		logger.info("송금완료후 Main화면이동");
		return "redirect:/main";
	}
	
//	@GetMapping("/success")
//	public String success() {
//		System.out.println("success");
//		return "success";
//	}
	//이용기관 부여번호 9자리 생성하는 메서드
	public String createNum() {
		String createNum="";// "468345554"
		for (int i = 0; i < 9; i++) {
			createNum+=((int)(Math.random()*10))+"";
		}
		System.out.println("이용기관부여번호9자리생성:"+createNum);
		return createNum;
	}
			
	//현재시간 구하는 메서드
	public String getDateTime() {
		LocalDateTime now = LocalDateTime.now();
		
		String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		
		return formatNow;
	}
}




