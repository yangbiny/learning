package com.impassive.rpc.exception;

public class BaseImpRcpException extends RuntimeException {

  public BaseImpRcpException() {
    super();
  }

  public BaseImpRcpException(String message) {
    super(message);
  }

  public BaseImpRcpException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseImpRcpException(Throwable cause) {
    super(cause);
  }

  protected BaseImpRcpException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
