package com.impassive.pay.exception;

/**
 * @author impassive
 */
public class ApplySignException extends RuntimeException {

  public ApplySignException(String message) {
    super(message);
  }

  public ApplySignException(Exception e) {
    super(e);
  }
}
