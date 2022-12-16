package com.impassive.pay.exception;

/**
 * @author impassive
 */
public class HttpException extends RuntimeException {

  public HttpException(String message) {
    super(message);
  }

  public HttpException(String message, Throwable cause) {
    super(message, cause);
  }
}
