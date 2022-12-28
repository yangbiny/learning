package com.impassive.pay.entity.notify.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

/**
 * @author impassivey
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Receipt {

  /**
   * 收据的信息
   */
  private List<InApp> inApp;


  /**
   * 原始的应用购买时间。
   *
   * <p>如果是自动续订订阅，表示订阅的初始购买时间</p>
   *
   * <p>对于同一个productId的产品，该时间始终保持不变</p>
   */
  private Long originalPurchaseDateMs;

}
