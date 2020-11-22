package com.kakaopay.divider.common.exception;

import com.kakaopay.divider.common.vo.ApiException;
import com.kakaopay.divider.common.vo.ApiResponse;
import com.kakaopay.divider.common.vo.ApiStatusCode;
import com.kakaopay.divider.money.vo.MoneyDivideRequest;
import com.kakaopay.divider.money.vo.MoneyRequest;
import com.kakaopay.divider.room.vo.RoomMemberRequest;
import com.kakaopay.divider.room.vo.RoomRequest;
import com.kakaopay.divider.user.vo.UserRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse validationExceptionResult(MethodArgumentNotValidException exception) {
        Object target = exception.getBindingResult().getTarget();
        ApiStatusCode status = ApiStatusCode.FAIL;

        if(target instanceof UserRequest) status = ApiStatusCode.INVALID_USER_REQUEST;
        if(target instanceof RoomRequest) status = ApiStatusCode.INVALID_ROOM_REQUEST;
        if(target instanceof RoomMemberRequest) status = ApiStatusCode.INVALID_JOIN_ROOM_REQUEST;
        if(target instanceof MoneyRequest) status = ApiStatusCode.INVALID_MONEY_REQUEST;
        if(target instanceof MoneyDivideRequest) status = ApiStatusCode.INVALID_MONEY_DIVIDE_REQUEST;

        ApiException apiException = new ApiException(status, exception.getMessage());
        return new ApiResponse(apiException);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse globalExceptionResult(Exception exception) {
        ApiException apiException = new ApiException(ApiStatusCode.FAIL, exception.getMessage());
        return new ApiResponse(apiException);
    }
}
