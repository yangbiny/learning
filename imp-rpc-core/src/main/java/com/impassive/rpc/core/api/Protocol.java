package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

public interface Protocol {

  /**
   * 服务暴露的接口，会将 url 里面的信息暴露 到注册中心中
   *
   * @param impUrl 需要暴露服务的 url 信息
   */
  void export(ImpUrl impUrl);

  <T> T refer(ImpUrl refer);

  void unExport(ImpUrl impUrl);

}
