package com.impassive.repository;

import com.impassive.entity.TestShardTableDo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author impassive
 */
@Repository
public interface TestTableRepository extends JpaRepository<TestShardTableDo, Long> {

  TestShardTableDo findTestShardTableDoByExternalIdAndId(Long externalId, Long id);

  @Query(value = "from TestShardTableDo where externalId > ?1")
  List<TestShardTableDo> findTestShardTableForRange(Long begin);
}
