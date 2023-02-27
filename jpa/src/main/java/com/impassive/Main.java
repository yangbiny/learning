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

    TestDo entity1 = new TestDo();
    entity1.setStatus(1);
    entity1.setExternalId(1L);
    entity1.setCreateAt(System.currentTimeMillis());
    TestDo save = testRepository.save(entity1);
    System.out.println(save);

    TestTableRepository tableRepository = context.getBean(TestTableRepository.class);

    TestShardTableDo testShardTableDoByAtlasId = tableRepository.findTestShardTableDoByExternalIdAndId(
        16L,
        5L
    );

    List<TestShardTableDo> testShardTableForRange = tableRepository.findTestShardTableForRange(1L);
    System.out.println(testShardTableForRange);

    System.out.println(testShardTableDoByAtlasId);

    TestShardTableDo entity = new TestShardTableDo();
    entity.setExternalId(2L);
    tableRepository.save(entity);
  }
}