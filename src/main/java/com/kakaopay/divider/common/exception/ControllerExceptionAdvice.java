package com.kakaopay.divider.common.exception;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.common.vo.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created By kjs4395 on 2020-11-21
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    public ApiResponse apiExceptionResult(ApiException exception) {
        return new ApiResponse(exception);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse globalExceptionResult(Exception exception) {
        ApiException apiException = new ApiException(exception.getMessage());
        return new ApiResponse(apiException);
    }
}
