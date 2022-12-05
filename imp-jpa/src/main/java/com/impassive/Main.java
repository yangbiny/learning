package com.impassive;

import com.impassive.entity.TestShardTable;
import com.impassive.repository.TestTableRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author impassive
 */
public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
        JpaConfig.class);

    TestTableRepository tableRepository = context.getBean(TestTableRepository.class);
    TestShardTable testShardTableByAtlasId = tableRepository.findTestTableByExternalIdAndId(
        16L,
        5L
    );
    System.out.println(testShardTableByAtlasId);
  }
}