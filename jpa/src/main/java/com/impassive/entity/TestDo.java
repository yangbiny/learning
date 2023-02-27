package com.impassive.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author impassive
 */
@Entity
@Table(name = "test_table_tag")
public class TestDo {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, name = "external_id")
  private Long externalId;

  @Column(nullable = false)
  private Integer status;


  @Column(nullable = false, name = "create_at")
  private Long createAt;

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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Long createAt) {
    this.createAt = createAt;
  }

}
