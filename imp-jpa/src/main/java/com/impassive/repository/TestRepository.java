package com.impassive.repository;

import com.impassive.entity.TestDo;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author impassive
 */
@Repository
public interface TestRepository extends JpaRepository<TestDo, Long> {

  @Query(value = "from TestDo where id > ?1")
  List<TestDo> atlasTagExtra(Long minId, Pageable pageable);

  @Query(value = "from TestDo ")
  List<TestDo> atlasTagOrderByIdDesc(Sort sort);

  /**
   * 使用原生SQL进行查询
   */
  @Query(nativeQuery = true, value = "select * from test_do where id in (?1)")
  List<TestDo> queryAtlasTagExtraByIdInOrderBy(List<Long> ids);

}
