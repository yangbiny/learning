package com.impassive.rpc.exception;

public class BaseImpRcpException extends RuntimeException {

  protected int exceptionCode;

  public BaseImpRcpException(int exceptionCode) {
    super();
    this.exceptionCode = exceptionCode;
  }

  public BaseImpRcpException(int exceptionCode, String message) {
    super(message);
    this.exceptionCode = exceptionCode;
  }

  public BaseImpRcpException(int exceptionCode, String message, Throwable cause) {
    super(message, cause);
    this.exceptionCode = exceptionCode;
  }

  public BaseImpRcpException(int exceptionCode, Throwable cause) {
    super(cause);
    this.exceptionCode = exceptionCode;
  }

}
