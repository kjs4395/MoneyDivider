package com.kakaopay.divider.money.vo;

import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
@Setter
public class MoneyRequest {
    @NotNull
    private Integer amount;
    @NotNull
    @Min(value = 1)
    private Integer divideNum;
    private MoneyRequestId id;

    public MoneyRequest with(MoneyRequestId moneyRequestId) {
        this.id = moneyRequestId;
        return this;
    }
}
