package com.impassive;

import com.google.common.collect.Lists;
import com.impassive.entity.TestDo;
import com.impassive.entity.TestShardTableDo;
import com.impassive.repository.TestRepository;
import com.impassive.repository.TestTableRepository;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author impassive
 */
public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
        JpaConfig.class);

    TestRepository testRepository = context.getBean(TestRepository.class);
    List<TestDo> testDos = testRepository.queryTestDoByIdInOrderBy(Lists.newArrayList(1L, 2L));
    System.out.println(testDos);

    TestTableRepository tableRepository = context.getBean(TestTableRepository.class);
    TestShardTableDo testShardTableDoByAtlasId = tableRepository.findTestShardTableDoByExternalIdAndId(
        16L,
        5L
    );
    System.out.println(testShardTableDoByAtlasId);

    TestShardTableDo entity = new TestShardTableDo();
    entity.setExternalId(2L);
    tableRepository.save(entity);
  }
}