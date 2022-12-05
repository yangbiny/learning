package com.impassive.repository;

import com.impassive.entity.TestShardTableDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author impassive
 */
@Repository
public interface TestTableRepository extends JpaRepository<TestShardTableDo, Long> {

  TestShardTableDo findTestTableByExternalId(Long externalId);

  TestShardTableDo findTestShardTableDoByExternalIdAndId(Long externalId, Long id);

}
