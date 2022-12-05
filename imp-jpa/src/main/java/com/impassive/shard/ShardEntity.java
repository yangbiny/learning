package com.impassive.shard;

/**
 * @author impassive
 */
public interface ShardEntity<T> extends Comparable<T> {

  T shardId();

}
