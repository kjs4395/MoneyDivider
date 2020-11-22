package com.kakaopay.divider.common.vo;

import lombok.Getter;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
public class ApiResponse {
    private final Result result;

    public ApiResponse() {
        this.result = new Result(true, ApiStatusCode.SUCCESS);
    }

    public ApiResponse(ApiException exception) {
        this.result = new Result(false, exception);
    }

    @Getter
    public static class Result {
        private final boolean isSuccess;
        private final ApiStatusCode status;
        private final int statusCode;
        private final String message;
        private String exceptionMessage;

        public Result(boolean isSuccess, ApiStatusCode status) {
            this.isSuccess = isSuccess;
            this.status = status;
            this.statusCode = status.getCode();
            this.message = status.getMesage();
        }

        public Result(boolean isSuccess, ApiException exception) {
            this(isSuccess, exception.getStatus());
            this.exceptionMessage = exception.getMessage();
        }
    }
}
