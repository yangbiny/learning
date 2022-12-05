package com.impassive.entity;

import com.impassive.shard.ShardEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author impassive
 */
@Entity
@Table(name = "test_shard")
public class TestShardTableDo implements ShardEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "external_id")
  private Long externalId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getExternalId() {
    return externalId;
  }

  public void setExternalId(Long atlasId) {
    this.externalId = atlasId;
  }

  @Override
  public String databaseName() {
    return "ds_01";
  }

  @Override
  public String magicTableName() {
    return "test_shard";
  }

  @Override
  public Long shardId() {
    return externalId;
  }

  @Override
  public String shardColumn() {
    return "external_id";
  }

  @Override
  public int compareTo(Long o) {
    return this.externalId.compareTo(o);
  }
}
