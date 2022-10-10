package com.impassive.rpc.config.common;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.utils.StringTools;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RegisterConfig implements ConfigurableData {

  private String address;

  private Integer port;

  private String path = "/imp/rpc/";

  @Override
  public boolean legal() {
    return StringTools.isNotEmpty(address) &&
        port != null && port > 0 &&
        StringTools.isNotEmpty(path);
  }
}
