package com.kakaopay.divider.money.interceptor.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@RequiredArgsConstructor
public class MoneyRequestId {
	@NotBlank
	private final Integer roomId;
	@NotNull
	private final Integer userId;
}
