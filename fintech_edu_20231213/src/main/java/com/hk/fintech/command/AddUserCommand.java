package com.hk.fintech.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddUserCommand {
	@NotBlank(message = "이름을 입력하세요")
	private String username;
	@NotBlank(message = "이메일을 입력하세요")
	private String useremail;
	@NotBlank(message = "비밀번호를 입력하세요")
	private String userpassword;
	@NotBlank(message = "인증 절차를 진행하세요")
	private String useraccesstoken;
	@NotBlank(message = "인증 절차를 진행하세요")
	private String userscope;
	@NotBlank(message = "인증 절차를 진행하세요")
	private String userseqno;
	
}
