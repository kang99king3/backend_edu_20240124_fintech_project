package com.hk.fintech.apidto;

import lombok.Data;

@Data
public class TokenResponseOobDto {
	private String access_token;
	private String token_type;
	private String expires_in;
	private String scope;
	private String client_use_code;
}
