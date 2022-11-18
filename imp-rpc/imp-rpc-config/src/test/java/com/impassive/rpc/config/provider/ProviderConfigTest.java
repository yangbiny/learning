package com.impassive.rpc.config.provider;

import com.impassive.rpc.config.common.ApplicationConfig;
import com.impassive.rpc.config.common.ProtocolConfig;
import com.impassive.rpc.config.common.RegisterConfig;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.extension.ExtensionLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProviderConfigTest {

  @BeforeEach
  void setUp() {
    ExtensionLoader<Protocol> protocolExtensionLoader = ExtensionLoader.buildExtensionLoader(
        Protocol.class);
    Protocol protocol = protocolExtensionLoader.buildDefaultExtension();
    System.out.println(protocol);
  }

  @Test
  void export() {
    TestInterface testInterface = new TestClass();
    ProviderConfig<TestInterface> testInterfaceProviderConfig = new ProviderConfig<>();
    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setPort(8081);
    testInterfaceProviderConfig.setProtocolConfig(protocolConfig);
    ApplicationConfig applicationConfig = new ApplicationConfig();
    applicationConfig.setApplicationName("test");
    RegisterConfig registerConfig = new RegisterConfig();
    registerConfig.setPort(2181);
    registerConfig.setAddress("localhost");
    testInterfaceProviderConfig.setRegisterConfig(registerConfig);
    testInterfaceProviderConfig.setApplicationConfig(applicationConfig);
    testInterfaceProviderConfig.setInvokeObject(testInterface);

    try {
      testInterfaceProviderConfig.export();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      testInterfaceProviderConfig.destroy();
    }
  }

  public interface TestInterface {

    void test();
  }

  public class TestClass implements TestInterface {

    @Override
    public void test() {
      System.out.println("test");
    }
  }
}