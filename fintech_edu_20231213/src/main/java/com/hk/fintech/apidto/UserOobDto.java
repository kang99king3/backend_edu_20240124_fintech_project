package com.hk.fintech.apidto;

import lombok.Data;

@Data
public class UserOobDto {
	private String access_token;
	private String scope;
	private String client_use_code;
}
