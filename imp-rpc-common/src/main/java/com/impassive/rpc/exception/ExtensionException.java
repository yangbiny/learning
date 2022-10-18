package com.impassive.rpc.exception;

public class ExtensionException extends BaseImpRcpException {

  public ExtensionException(int exceptionCode) {
    super(exceptionCode);
  }

  public ExtensionException(int exceptionCode, String message) {
    super(exceptionCode, message);
  }

  public ExtensionException(int exceptionCode, String message, Throwable cause) {
    super(exceptionCode, message, cause);
  }

  public ExtensionException(int exceptionCode, Throwable cause) {
    super(exceptionCode, cause);
  }
}
