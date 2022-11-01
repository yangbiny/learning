package com.impassive.rpc.core.registry;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.common.URLRegisterAddress;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import com.impassive.rpc.utils.AddressTools;
import com.impassive.rpc.utils.CollectionTools;
import com.impassive.rpc.utils.JsonTools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

@Slf4j
public class ZookeeperRegistry implements Registry {

  private CuratorFramework zookeeperClient;

  public ZookeeperRegistry(ImpUrl<?> impUrl) {
    this.zookeeperClient = initClient(impUrl);
  }

  @Override
  public void register(ImpUrl<?> impUrl) {
    String format = buildRegisterPath(impUrl);
    try {
      Set<String> ips = new HashSet<>();
      ips.add(AddressTools.getIpAddress());
      Stat stat = zookeeperClient.checkExists().forPath(format);
      if (stat != null) {
        byte[] bytes = zookeeperClient.getData().forPath(format);
        List<String> lists = JsonTools.fromJsonWithRaw(new String(bytes), String.class);
        ips.addAll(lists);
        zookeeperClient.setData()
            .forPath(format, JsonTools.toJson(ips).getBytes());
      } else {
        zookeeperClient.create()
            .creatingParentsIfNeeded()
            .withMode(CreateMode.PERSISTENT)
            .forPath(format, JsonTools.toJson(ips).getBytes());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void unRegister(ImpUrl<?> impUrl) {
    String path = buildRegisterPath(impUrl);
    try {
      Stat stat = zookeeperClient.checkExists().forPath(path);
      if (stat == null) {
        return;
      }
      byte[] bytes = zookeeperClient.getData().forPath(path);
      List<String> strings = JsonTools.fromJsonWithRaw(new String(bytes), String.class);
      strings.remove(AddressTools.getIpAddress());
      if (CollectionTools.isEmpty(strings)) {
        zookeeperClient.delete().forPath(path);
      } else {
        zookeeperClient.setData().forPath(path, JsonTools.toJson(strings).getBytes());
      }

    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, e);
    }
  }

  private CuratorFramework initClient(ImpUrl<?> impUrl) {
    URLRegisterAddress registerAddress = impUrl.getRegisterAddress();
    try {
      String address = String.format("%s:%s", registerAddress.address(),
          registerAddress.port());

      zookeeperClient = CuratorFrameworkFactory.newClient(address,
          new ExponentialBackoffRetry(1000, 10));
      zookeeperClient.start();
      return zookeeperClient;
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, e);
    }
  }

  private String buildRegisterPath(ImpUrl<?> impUrl) {
    return String.format("%s/%s/%s/provider",
        impUrl.getRegisterAddress().path(),
        impUrl.getApplication().applicationName(),
        impUrl.getClassType().getSimpleName());
  }

}
