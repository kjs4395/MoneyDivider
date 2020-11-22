package com.kakaopay.divider.common.vo;

import lombok.Getter;

/**
 * Created By kjs4395 on 2020-11-21
 */
@Getter
public class ApiException extends RuntimeException {
    private final ApiStatusCode status;

    public ApiException(ApiStatusCode status) {
        this.status = status;
    }

    public ApiException(ApiStatusCode status, String message) {
        super(message);
        this.status = status;
    }
}
