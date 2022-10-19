package com.impassive.rpc.exception;

public class ServiceException extends BaseImpRcpException {

  public ServiceException(int exceptionCode, String message) {
    super(exceptionCode, message);
  }
}
