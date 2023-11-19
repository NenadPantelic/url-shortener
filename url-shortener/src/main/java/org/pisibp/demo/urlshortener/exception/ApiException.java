package org.pisibp.demo.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ApiException extends RuntimeException {

    public static ApiException BAD_REQUEST_EXCEPTION = new ApiException("Bad request.", 400);
    public static ApiException NOT_FOUND_EXCEPTION = new ApiException("Not found.", 404);
    public static ApiException INTERNAL_SERVER_ERROR_EXCEPTION = new ApiException("Internal server error.", 500);

    private final String message;
    private final int statusCode;
}
