package com.impassive.rpc.exception;

public class ExceptionCode {

  /**
   * 参数配置问题
   */
  public static int CONFIG_EXCEPTION = 1000;

  /**
   * 扩展点相关的异常
   */
  public static int EXTENSION_EXCEPTION = 2000;

  /**
   * 扩展点相关文件加载失败
   */
  public static int EXTENSION_FILE_LOAD_EXCEPTION = 2001;

  /**
   * 扩展点 配置问题
   */
  public static int EXTENSION_CONFIG_EXCEPTION = 2002;

  /**
   * 服务暴露的相关异常
   */
  public static int SERVICE_EXPORTER_EXCEPTION = 3000;
}
