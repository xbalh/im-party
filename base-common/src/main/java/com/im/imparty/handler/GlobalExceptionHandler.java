package com.im.imparty.handler;

import com.im.imparty.common.exception.CustomException;
import com.im.imparty.web.vo.DataResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public DataResult businessException(CustomException customException) {
        return DataResult.error(customException.getErrorCode(), customException.getMessage());
    }
}
