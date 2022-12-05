package com.impassive.shard;

import java.util.Collection;
import java.util.Properties;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

/**
 * @author impassive
 */
public class CustomShardingAlgorithm<T extends ShardEntity<T>> implements StandardShardingAlgorithm<T> {

  @Override
  public Properties getProps() {
    return null;
  }


  @Override
  public void init(Properties properties) {

  }

  @Override
  public String getType() {
    return "impassive";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<T> shardingValue) {
    T value = shardingValue.getValue();

    T t = value.shardId();
    return null;
  }

  @Override
  public Collection<String> doSharding(Collection<String> availableTargetNames,
      RangeShardingValue<T> shardingValue) {
    return null;
  }
}
