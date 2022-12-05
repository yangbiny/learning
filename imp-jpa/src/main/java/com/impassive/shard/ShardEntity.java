package com.impassive.shard;

/**
 * @author impassive
 */
public interface ShardEntity extends Comparable<Long> {

  String databaseName();

  String magicTableName();

  String shardColumn();

  /**
   * 分片总数
   *
   * @return 16
   */
  int shardCnt();

  /**
   * 进行分表的字段值
   */
  Long shardId();

}
