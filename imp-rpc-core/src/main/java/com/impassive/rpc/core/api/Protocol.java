package com.impassive.rpc.core.api;

import com.impassive.rpc.common.URL;

public interface Protocol {

  /**
   * 服务暴露的接口，会将 url 里面的信息暴露 到注册中心中
   *
   * @param url 需要暴露服务的 url 信息
   */
  void export(URL<?> url);

  <T> T refer(URL<T> refer);

}
