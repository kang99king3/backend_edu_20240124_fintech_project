package com.hk.fintech.dtos;

import org.apache.ibatis.type.Alias;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Alias("AuthTokenDto")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthTokenDto {

	private int token_seq;//토큰구분번호(PK)
	private int user_seq;//회원번호(FK)
	private String accesstoken;//토큰값
	private String scope;//권한등급
}
