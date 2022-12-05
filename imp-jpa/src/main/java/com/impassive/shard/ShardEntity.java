package com.impassive.shard;

/**
 * @author impassive
 */
public interface ShardEntity<T> extends Comparable<T> {

  String databaseName();

  String magicTableName();

  T shardId();

  String shardColumn();

}
