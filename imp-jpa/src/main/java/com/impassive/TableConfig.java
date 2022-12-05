package com.impassive;

import lombok.Getter;
import lombok.ToString;

/**
 * @param magicTableName 逻辑表名：test_shard
 * @param shardColumn 进行分表的列：external_id
 * @param shardCnt 分表的张数：16 --> 分为 16张表
 * @author impassive
 */
@Getter
@ToString
public record TableConfig(
    String database,
    String magicTableName,
    String shardColumn,
    Integer shardCnt) {

}
