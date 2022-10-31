package com.impassive.rpc.core.registry;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.common.URLRegisterAddress;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

@Slf4j
public class ZookeeperRegistry implements Registry {

  private final AtomicBoolean init = new AtomicBoolean(false);

  private CuratorFramework zookeeperClient;

  public ZookeeperRegistry(ImpUrl<?> impUrl) {
    this.zookeeperClient = initClient(impUrl);
  }

  @Override
  public void register(ImpUrl<?> impUrl) {
    String format = buildRegisterPath(impUrl);
    try {
      Stat stat = zookeeperClient.checkExists().forPath(format);
      if (stat != null) {
        log.error("duplicated exported service : {}", impUrl.getClassType());
        throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION,
            "service has exported");
      }
      zookeeperClient.create()
          .creatingParentsIfNeeded()
          .withMode(CreateMode.PERSISTENT)
          .forPath(format);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private String buildRegisterPath(ImpUrl<?> impUrl) {
    return String.format("%s/%s/%s/provider",
        impUrl.getRegisterAddress().path(),
        impUrl.getApplication().applicationName(),
        impUrl.getClassType().getSimpleName());
  }

  @Override
  public void unRegister(ImpUrl<?> impUrl) {

  }

  private CuratorFramework initClient(ImpUrl<?> impUrl) {
    if (init.get()) {
      return null;
    }
    URLRegisterAddress registerAddress = impUrl.getRegisterAddress();
    try {
      if (init.compareAndSet(false, true)) {
        String address = String.format("%s:%s", registerAddress.address(),
            registerAddress.port());

        zookeeperClient = CuratorFrameworkFactory.newClient(address,
            new ExponentialBackoffRetry(1000, 10));
        zookeeperClient.start();
      }
      return zookeeperClient;
    } catch (Exception e) {
      init.set(false);
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, e);
    }
  }

}
