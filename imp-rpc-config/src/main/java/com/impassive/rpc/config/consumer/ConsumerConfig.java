package com.impassive.rpc.config.consumer;

import com.impassive.rpc.config.BaseConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumerConfig extends BaseConfig {

  @Override
  public boolean legal() {
    return this.applicationConfig != null && this.registerConfig != null;
  }
}
