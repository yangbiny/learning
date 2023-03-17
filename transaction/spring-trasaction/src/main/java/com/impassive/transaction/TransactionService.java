package com.impassive.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author impassive
 */
@Service
public class TransactionService {


  @Transactional
  public void test() {

  }


}
