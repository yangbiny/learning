package com.impassive.pay.cmd;

import com.impassive.pay.core.Rmb;
import com.impassive.pay.entity.AliProductCode;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@ToString
public class CreatePaySignCmd {

  private String paymentNo;

  private String inventoryName;

  private Rmb amount;

  private AliProductCode productCode;

  private Date createAt;

  public boolean checkIsIllegal() {
    return StringUtils.isAnyEmpty(paymentNo, inventoryName) || amount == null
        || productCode == null;
  }
}