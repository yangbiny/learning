package com.impassive.rpc.common;

/**
 * 可配置的数据对象，都需要实现该接口
 */
public interface ConfigurableData {

  /**
   * 参数是否是非法参数
   *
   */
  void checkIllegal();

}
