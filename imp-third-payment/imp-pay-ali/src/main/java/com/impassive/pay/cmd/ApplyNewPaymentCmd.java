package com.impassive.pay.cmd;

import com.impassive.pay.core.Rmb;
import lombok.Data;

@Data
public class ApplyNewPaymentCmd {

  private String paymentNo;

  private Rmb totalAmount;

  private String inventoryName;

  private String agreementNo;


}