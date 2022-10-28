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
import org.apache.zookeeper.CreateMode;

public class ZookeeperRegistry implements Registry {

  private final AtomicBoolean init = new AtomicBoolean(false);

  private CuratorFramework zookeeperClient;

  @Override
  public void register(URL<?> url) {
    URLRegisterAddress registerAddress = url.getRegisterAddress();
    initClient(registerAddress);
    String format = String.format("/imp/rpc/%s/%s/provider",
        url.getApplication().applicationName(),
        url.getClassType().getSimpleName());

    StringBuilder sb = new StringBuilder();
    try {

      boolean first = false;
      for (char c : format.toCharArray()) {
        if (c == '/' && first) {
          String path = zookeeperClient.create()
              .withMode(CreateMode.PERSISTENT)
              .forPath(sb.toString());
          System.out.println(path);
        }
        if (c == '/') {
          first = true;
        }
        sb.append(c);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


  }

  private void initClient(URLRegisterAddress registerAddress) {
    if (init.get()) {
      return;
    }
    try {
      if (init.compareAndSet(false, true)) {
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
