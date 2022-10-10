package com.impassive.rpc.config;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.config.common.ApplicationConfig;
import com.impassive.rpc.config.common.RegisterConfig;
import lombok.Data;

@Data
public abstract class BaseConfig implements ConfigurableData {

  protected ApplicationConfig applicationConfig;

  protected RegisterConfig registerConfig;


}
