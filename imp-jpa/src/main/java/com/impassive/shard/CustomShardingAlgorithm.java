package com.impassive.shard;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

/**
 * @author impassive
 */
public class CustomShardingAlgorithm<T extends Long> implements
    StandardShardingAlgorithm<T> {

  private Properties properties;

  @Override
  public Properties getProps() {
    return properties;
  }


  @Override
  public void init(Properties properties) {
    Set<Object> objects = properties.keySet();
    for (Object object : objects) {
      Object o = properties.get(object);
      if (o == null || !StringUtils.isNumeric(o.toString())) {
        throw new RuntimeException("分片数必须是个整数");
      }
    }
    this.properties = properties;
  }

  @Override
  public String getType() {
    return "impassive";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<T> shardingValue) {
    Object value = shardingValue.getValue();

    Long shardValue = null;
    int shardCnt = 0;
    if (value instanceof Long longValue) {
      String logicTableName = shardingValue.getLogicTableName();
      Object temp = properties.get(logicTableName);
      if (temp == null || !StringUtils.isNumeric(temp.toString())) {
        throw new RuntimeException("无法进行分片 : " + logicTableName);
      }
      shardValue = longValue;
      shardCnt = Integer.parseInt(temp.toString());
    }

    if (shardValue == null || shardCnt <= 0) {
      throw new RuntimeException("无法进行分片");
    }

    String prefix = shardingValue.getDataNodeInfo().getPrefix();
    return String.format(prefix + shardValue % shardCnt + "");
  }

  @Override
  public Collection<String> doSharding(Collection<String> availableTargetNames,
      RangeShardingValue<T> shardingValue) {
    return null;
  }
}
