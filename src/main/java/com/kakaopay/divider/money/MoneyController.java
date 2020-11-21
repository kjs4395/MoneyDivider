package com.kakaopay.divider.money;

import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import com.kakaopay.divider.money.service.MoneyDivideInfoService;
import com.kakaopay.divider.money.service.MoneyService;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyDivideResponse;
import com.kakaopay.divider.money.vo.MoneyRequest;
import com.kakaopay.divider.money.vo.MoneyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RequestMapping("/money")
@RequiredArgsConstructor
@RestController
public class MoneyController {
    private final MoneyService moneyService;
    private final MoneyDivideInfoService moneyDivideInfoService;

    /**
     * 뿌리기 정보 등록
     *  - 인원 수 대로 뿌릴 금액도 미리 등록
     *
     * @param moneyRequestId    - 요청 ID 정보
     * @param moneyRequest      - 뿌리기 요청 정보
     * @return
     */
    @PostMapping({"", "/"})
    public MoneyResponse generateMoneyForDivide(@Valid @RequestAttribute MoneyRequestId moneyRequestId,
                                                @Valid @RequestBody MoneyRequest moneyRequest) {
        JMoney money = moneyService.generateMoneyForDivide(moneyRequest.with(moneyRequestId));
        return new MoneyResponse(money);
    }

    /**
     * 뿌린 금액 받기
     * @param moneyRequestId        - 요청 ID 정보
     * @param moneyDivideRequest    - 받기 요청 정보
     * @return
     */
    @PutMapping("/divide")
    public MoneyDivideResponse updateDivide(@Valid @RequestAttribute MoneyRequestId moneyRequestId,
                                            @Valid @RequestBody MoneyDivideRequest moneyDivideRequest) {
        JMoneyDivideInfo divideInfo = moneyDivideInfoService.receiveMoneyDivide(moneyDivideRequest.with(moneyRequestId));
        return new MoneyDivideResponse(divideInfo);
    }
}
