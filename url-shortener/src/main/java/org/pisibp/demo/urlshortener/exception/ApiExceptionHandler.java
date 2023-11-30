package org.pisibp.demo.urlshortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<ApiError> handleApiException(final ApiException e,
                                                             final WebRequest request) {
        return new ResponseEntity<>(
                new ApiError(e.getMessage(), e.getStatusCode()),
                HttpStatusCode.valueOf(e.getStatusCode())
        );

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        final String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(
                new ApiError(errorMessage, ApiException.BAD_REQUEST_EXCEPTION.getStatusCode()),
                HttpStatusCode.valueOf(ApiException.BAD_REQUEST_EXCEPTION.getStatusCode())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ApiError> handleRuntimeException(final RuntimeException e,
                                                                 final WebRequest request) {
        log.error("An unexpected exception occurred.", e);
        return handleApiException(ApiException.INTERNAL_SERVER_ERROR_EXCEPTION, request);
    }
}