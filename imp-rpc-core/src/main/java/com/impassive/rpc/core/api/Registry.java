package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

public interface Registry {

  /**
   * 将 url 的信息，写入到注册中心
   *
   * @param impUrl url
   */
  void register(ImpUrl<?> impUrl);

  /**
   * 从 注册中心 删除 当前url的信息。如果是 多个服务器，只会删除当前的IP
   *
   * @param impUrl url
   */
  void unRegister(ImpUrl<?> impUrl);

}
