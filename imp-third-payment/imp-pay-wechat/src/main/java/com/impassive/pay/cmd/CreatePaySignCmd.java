package com.impassive.pay.cmd;

import com.impassive.pay.core.Rmb;
import lombok.Data;

/**
 * @author impassive
 */
@Data
public class CreatePaySignCmd {

  private String paymentNo;

  private String describe;

  private Rmb amount;

}
