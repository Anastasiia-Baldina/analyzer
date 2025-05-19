package ru.vse.file.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vse.file.dto.ErrorResponse;
import ru.vse.file.gateway.exception.InternalServiceException;

@RestControllerAdvice
public class ErrorHandlerController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = InternalServiceException.class, produces = "application/json")
    public ErrorResponse handleInternalServiceException(InternalServiceException e) {
        return new ErrorResponse()
                .setStatus(e.httpStatus())
                .setMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class, produces = "application/json")
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponse()
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class, produces = "application/json")
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMessage(e.getMessage());
    }
}
