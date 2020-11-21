package com.kakaopay.divider.money.interceptor.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@RequiredArgsConstructor
public class MoneyRequestId {
	private final String roomId;
	private final Integer userId;
}
