package com.impassive.rpc.core.registry;

import com.impassive.rpc.common.URL;
import com.impassive.rpc.common.URLRegisterAddress;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperRegistry implements Registry {

  private final AtomicBoolean init = new AtomicBoolean(false);

  private CuratorFramework zookeeperClient;

  @Override
  public void register(URL<?> url) {
    URLRegisterAddress registerAddress = url.getRegisterAddress();
    initClient(registerAddress);

  }

  private void initClient(URLRegisterAddress registerAddress) {
    if (init.get()) {
      return;
    }
    try {
      if (init.compareAndExchange(false, true)) {
        String address = String.format("%s:%s/%s", registerAddress.address(),
            registerAddress.port(),
            registerAddress.path());

        zookeeperClient = CuratorFrameworkFactory.newClient(address,
            new ExponentialBackoffRetry(1000, 10));
        zookeeperClient.start();
      }
    } catch (Exception e) {
      init.set(false);
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, e);
    }
  }

}
