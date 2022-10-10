package com.impassive.rpc.config.common;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.utils.StringTools;
import lombok.Data;

@Data
public class ApplicationConfig implements ConfigurableData {

  private String applicationName;

  @Override
  public boolean legal() {
    return StringTools.isNotEmpty(applicationName);
  }
}
