package com.impassive.shard;

import java.util.Collection;
import java.util.Properties;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;

/**
 * @author impassive
 */
public class EntityShard<T extends ShardEntity<T>> implements ShardingAlgorithm,
    StandardShardingAlgorithm<T> {

  @Override
  public Properties getProps() {
    return null;
  }


  @Override
  public void init(Properties properties) {

  }

  @Override
  public String getType() {
    return "STANDARD_TEST_TBL";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<T> shardingValue) {
    return null;
  }

  @Override
  public Collection<String> doSharding(Collection<String> availableTargetNames,
      RangeShardingValue<T> shardingValue) {
    return null;
  }
}
