package com.apptive.devlog.global.response.error.handler;

import com.apptive.devlog.global.response.error.exception.*;
import com.apptive.devlog.global.response.error.model.ErrorCode;
import com.apptive.devlog.global.response.error.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(
                        errorCode.getStatus(),
                        errorCode.getMessage(),
                        errorCode.getCode()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return buildResponse(ErrorCode.DUPLICATE_EMAIL);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPasswordException ex) {
        return buildResponse(ErrorCode.INVALID_PASSWORD);
    }

    @ExceptionHandler(InvalidProviderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProvider(InvalidProviderException ex) {
        return buildResponse(ErrorCode.INVALID_PROVIDER);
    }

    @ExceptionHandler(InvalidStateFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidStateFormatException ex) {
        return buildResponse(ErrorCode.INVALID_STATE_FORMAT);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return buildResponse(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        "BAD_REQUEST"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(),
                        "INTERNAL_ERROR"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return buildResponse(ErrorCode.UNKNOWN_ERROR);
    }
}
