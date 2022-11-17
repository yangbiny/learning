package com.impassive.rpc.common;

import lombok.Getter;

/**
 * @author impassive
 */
@Getter
public enum ConfigKeyPath {

  application("application_config"),

  registry("registry_config"),

  provider("provider_config"),

  consumer("consumer_config"),

  protocol("protocol_config");

  private final String path;

  ConfigKeyPath(String path) {
    this.path = path;
  }
}
