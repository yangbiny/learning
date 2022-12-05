package com.impassive.entity;

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
public class TestShardTableDo {

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

}
