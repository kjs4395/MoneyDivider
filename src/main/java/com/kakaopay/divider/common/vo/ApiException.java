package com.kakaopay.divider.common.vo;

/**
 * Created By kjs4395 on 2020-11-21
 */
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
