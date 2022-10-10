package com.impassive.rpc.common;

/**
 * 可配置的数据对象，都需要实现该接口
 */
public interface ConfigurableData {

  /**
   * 配置是否合法
   *
   * @return true:合法
   */
  boolean legal();

}
