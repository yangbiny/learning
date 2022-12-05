package com.impassive.repository;

import com.impassive.entity.TestShardTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Component
public interface TestTableRepository extends JpaRepository<TestShardTable, Long> {

  TestShardTable findTestTableByExternalId(Long externalId);

  TestShardTable findTestTableByExternalIdAndId(Long externalId, Long id);

}
