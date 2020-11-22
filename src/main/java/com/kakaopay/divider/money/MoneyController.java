package com.kakaopay.divider.money;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoney;
import com.kakaopay.divider.domain.jooq.tables.pojos.JMoneyDivideInfo;
import com.kakaopay.divider.money.interceptor.vo.MoneyRequestId;
import com.kakaopay.divider.money.service.MoneyDivideInfoService;
import com.kakaopay.divider.money.service.MoneyService;
import com.kakaopay.divider.money.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
     *
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

    /**
     * 뿌리기 정보 조회
     *
     * @param moneyRequestId    - 요청 ID 정보
     * @param token             - 조회 요청 정보 (token에 특수문자가 포함돼 GET 요청 시 URL 인코딩 처리)
     * @return
     */
    @GetMapping("/{token}")
    public MoneyInfoReseponse getMoneyInfo(@Valid @RequestAttribute MoneyRequestId moneyRequestId,
                                           @PathVariable String token) throws UnsupportedEncodingException {
        String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8.name());
        if(decodedToken.length() != 3) {
            throw new ApiException("유효하지 않은 토큰입니다.");
        }
        return new MoneyInfoReseponse(moneyService.getMoneyInfo(moneyRequestId, decodedToken));
    }
}
