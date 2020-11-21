package com.kakaopay.divider.money.vo;

import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@RequiredArgsConstructor
public class MoneyDivideResponse {
	private final JMoneyDivideInfo moneyDivideInfo;
}
