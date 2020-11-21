package com.kakaopay.divider.money.vo;

import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@Setter
public class MoneyDivideRequest {
	@NotBlank
	@Size(min = 3, max = 3)
	private String token;
	private MoneyRequestId id;

	public MoneyDivideRequest with(MoneyRequestId moneyRequestId) {
		this.id = moneyRequestId;
		return this;
	}
}
