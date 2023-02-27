package com.impassive.rpc.config.common;

import com.impassive.rpc.common.ConfigKeyPath;
import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import lombok.Data;

@Data
public class ProtocolConfig implements ConfigurableData {

  /**
   * 点对点调用需要，非必须
   */
  private String address;

  private Integer port;

  private String name;


  @Override
  public ConfigKeyPath keyPath() {
    return ConfigKeyPath.protocol;
  }

  @Override
  public void checkIllegal() {
    if (this.port == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION, "服务端口不能为空");
    }
  }
}
