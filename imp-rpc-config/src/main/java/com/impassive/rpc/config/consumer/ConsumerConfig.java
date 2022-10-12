package com.impassive.rpc.config.consumer;

import com.impassive.rpc.config.BaseConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumerConfig extends BaseConfig {

  @Override
  public void checkIllegal() {
    super.checkIllegal();
  }
}
