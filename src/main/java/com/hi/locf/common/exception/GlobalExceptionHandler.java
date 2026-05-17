package com.hi.locf.common.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.common.api.ErrorResponse;
import com.hi.locf.common.code.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(new ErrorResponse(errorCode.getCode(), exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = buildValidationMessage(exception.getBindingResult().getFieldErrors());

        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(new ErrorResponse(ErrorCode.INVALID_REQUEST.getCode(), message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.fail(new ErrorResponse(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                )));
    }

    private String buildValidationMessage(List<FieldError> fieldErrors) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            if (message.length() > 0) {
                message.append(", ");
            }
            message.append(fieldError.getDefaultMessage());
        }
        return message.toString();
    }
}
