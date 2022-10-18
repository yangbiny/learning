package com.impassive.rpc.config.provider;

import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.extension.ExtensionLoader;
import org.junit.jupiter.api.Test;

class ProviderConfigTest {

  @Test
  void export() {

    ExtensionLoader<Protocol> protocolExtensionLoader = ExtensionLoader.buildExtensionLoader(
        Protocol.class);
    Protocol protocol = protocolExtensionLoader.buildDefaultExtension();
    System.out.println(protocol);
  }
}