package com.kakaopay.divider.common.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
public class ApiResponse {
    private final Result result;

    public ApiResponse() {
        this.result = new Result(true, "SUCCESS");
    }

    public ApiResponse(ApiException exception) {
        this.result = new Result(false, exception.getMessage());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final boolean isSuccess;
        private final String message;
    }
}
