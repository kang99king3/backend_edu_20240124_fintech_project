package com.hk.fintech.apidto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TokenResponseDto {
  private String access_token;
  private String token_type;
  private String refresh_token;
  private String expires_in;
  private String scope;
  private String user_seq_no;  
}
