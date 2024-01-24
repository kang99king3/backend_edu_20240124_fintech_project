package com.hk.fintech.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginCommand {
	@NotBlank(message = "email을 입력하세요")
	private String email;
	@NotBlank(message = "비밀번호를 입력하세요")
	private String password;
}
