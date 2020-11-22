package com.kakaopay.divider.money.vo;

import com.kakaopay.divider.common.vo.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created By kjs4395 on 2020-11-22
 */
@Getter
@RequiredArgsConstructor
public class MoneyInfoReseponse extends ApiResponse {
    private final MoneyInfo moneyInfo;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class MoneyInfo {
        private final Integer roomId;
        private final String token;
        private final Integer amount;
        private final Integer receiveAmount;
        private final MoneyState state;
        private final LocalDateTime createDate;
        private final List<ReceiveUser> receiveUserList;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ReceiveUser {
        private final Integer amount;
        private final Integer userId;
        private final String userName;
        private final LocalDateTime receiveDate;
    }
}
