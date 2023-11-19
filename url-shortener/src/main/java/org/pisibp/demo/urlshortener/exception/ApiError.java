package org.pisibp.demo.urlshortener.exception;

public record ApiError(String message, int statusCode) {

}
