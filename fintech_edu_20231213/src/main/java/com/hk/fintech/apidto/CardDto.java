package com.hk.fintech.apidto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CardDto {

	private String card_id;         //카드 식별자
	private String card_num_masked; //마스킹된 카드 번호
	private String card_name;       //상품명
	private String card_member_type;//본인/가족 구분
	
}
