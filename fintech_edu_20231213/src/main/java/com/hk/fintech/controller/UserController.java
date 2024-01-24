package com.hk.fintech.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.fintech.apidto.AccountCancelDto;
import com.hk.fintech.apidto.AccountCancelReqDto;
import com.hk.fintech.apidto.AccountListDto;
import com.hk.fintech.apidto.CardListDto;
import com.hk.fintech.apidto.TokenResponseDto;
import com.hk.fintech.apidto.UserMeDto;
import com.hk.fintech.command.AddUserCommand;
import com.hk.fintech.command.LoginCommand;
import com.hk.fintech.dtos.AuthTokenDto;
import com.hk.fintech.dtos.UserDto;
import com.hk.fintech.feign.OpenBankingFeign;
import com.hk.fintech.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger=LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private OpenBankingFeign openBankingFeign;
    @Autowired
    private UserService userService;
   
    //회원가입폼 이동하기
	@GetMapping("/signup")
	public String registForm(Model model) {
		logger.info("회원가입폼이동");
		model.addAttribute("addUserCommand", new AddUserCommand());
		return "user/signup";
	}
	
	//회원가입 진행시: 인증후 토큰 요청
	@GetMapping("/authresult")
	public String authResult(String code, Model model) throws IOException, ParseException {
		logger.info("회원가입을 위한 인증코드:"+code);
		
//		//토큰요청(권한:scope=login inquiry transfer)
		TokenResponseDto tokenDto= openBankingFeign.requestToken(code, 
								    "4987e938-f84b-4e23-b0a2-3b15b00f4ffd", 
								    "3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec", 
								    "http://localhost:8070/user/authresult", 
									"authorization_code");
		System.out.println(tokenDto);
		model.addAttribute("tokenDto", tokenDto);
		
		return "user/authresult";
	}

	//카드서비스 요청을 위한 토큰 발급
	@GetMapping("/card_token")
	public String cardToken(String code, Model model,HttpServletRequest request) throws IOException, ParseException {
		logger.info("카드등록및조회 권한 토큰을 위한 인증코드:"+code);
		
//		//토큰요청(권한:scope=login inquiry transfer)
		TokenResponseDto tokenDto= openBankingFeign.requestToken(code, 
								    "4987e938-f84b-4e23-b0a2-3b15b00f4ffd", 
								    "3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec", 
								    "http://localhost:8070/user/card_token", 
									"authorization_code");
		System.out.println(tokenDto);
		
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		int user_seq=ldto.getUser_seq();
		
		
		userService.addToken(tokenDto,user_seq);
		
		return "user/authcard_complete";
	}
	
	//회원가입하기
	@PostMapping("/adduser")
	public String addUser(@Validated AddUserCommand command,BindingResult result) {
		if(result.hasErrors()) {
			logger.info("회원정보를 정확히 입력하세요");
			return "user/signup";
		}
		logger.info("회원가입하기");
		System.out.println(command);
		userService.addUser(command);
		return "index";
	}
	
	//로그인하기
	@PostMapping("/login")
	public String login(@Validated LoginCommand command,BindingResult result, HttpServletRequest request) {
		if(result.hasErrors()) {
			logger.info("email이나 password를 입력하세요");
			return "index";
		}
		logger.info("회원로그인하기");
		System.out.println(command);
		String path=userService.loginUser(command, request);
		return "redirect:"+path;
	}
	
	//로그아웃하기
	@GetMapping("/logout")
	public String logOut(HttpServletRequest request) {
		logger.info("회원로그아웃");
		HttpSession session=request.getSession();
		session.invalidate();

		return "redirect:/";
	}
	
	//나의 정보 조회
	@GetMapping("/myinfo")
	public String myInfo(HttpServletRequest request,
						Model model) {
		logger.info("나의 정보조회");
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		
		UserMeDto myInfoDto=
		openBankingFeign.requestUserMe("Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken()
									 , ldto.getUserseqno()+"");
		System.out.println(myInfoDto);
		model.addAttribute("myInfoDto", myInfoDto);
		
		//나의정보 화면에서 카드 조회시 인증 필요한지 여부를 알려주기 위해 전달(cardtoken이 있으면 인증 필요없음)
		AuthTokenDto authTokenDto=userService.getCardToken(ldto.getUser_seq());
		model.addAttribute("authTokenDto", authTokenDto);
		System.out.println(authTokenDto);
		
		//카드권한 토큰이 있다면 카드목록 요청하기
		if(authTokenDto!=null) {
			CardListDto cardListDto=openBankingFeign.requestAccountCardList("Bearer "+ldto.getAuthtokenlist().get(2).getAccesstoken(),
													"M202201886U"+createNum(),                     
													 ldto.getUserseqno()+"",
													 "041", //우리카드 코드
													 "041");	
			model.addAttribute("cardListDto", cardListDto);
		} 
		return "user/myinfo";
	}
	
	//계좌해지하기
	@GetMapping("/accountcancel")
	public String accountCancel(HttpServletRequest request,
			Model model, String fintech_use_num) {
		System.out.println("계좌해지");
		System.out.println("핀테크이용번호:"+fintech_use_num);
//		String bank_tran_id="M202201886U"+createNum();
		String inquiryScope="inquiry";
		String transferScope="transfer";
		
		HttpSession session=request.getSession();
		UserDto ldto=(UserDto)session.getAttribute("ldto");
		
		AccountCancelDto accountCancelDto1=
		openBankingFeign.requestAccountCancel("Bearer "+ldto.getAuthtokenlist().get(0).getAccesstoken(),				                              
				           new AccountCancelReqDto("M202201886U"+createNum(),inquiryScope,fintech_use_num));
//		AccountCancelDto accountCancelDto2=
//		openBankingFeign.requestAccountCancel("Bearer "+ldto.getAuthtokendto().getAccesstoken(),				                              
//		           new AccountCancelReqDto("M202201886U"+createNum(),transferScope,fintech_use_num));
		System.out.println(accountCancelDto1);
//		System.out.println(accountCancelDto2);
		return "redirect:/user/myinfo";
	}
	
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






