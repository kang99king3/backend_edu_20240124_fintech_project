package com.hk.fintech.apidto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequestOobDto {
	private String client_id;
	private String client_secret;
	private String scope;
	private String grant_type;
}
