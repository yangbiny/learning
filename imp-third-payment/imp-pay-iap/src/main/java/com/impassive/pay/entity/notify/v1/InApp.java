package com.impassive.pay.entity.notify.v1;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassivey
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InApp {

  // 传入的 自定义信息
  private String appAccountToken;

  // 退款相关信息
  /**
   * App Store 退还交易或从家庭共享中撤销交易的时间，采用类似于 ISO 8601 的日期时间格式。此字段仅适用于已退款或撤销的交易。
   */
  private String cancellationDate;

  /**
   * 同上的 毫秒格式
   */
  private Long cancellationDateMs;

  private String cancellationDatePst;

  private String cancellationReason;

  /**
   * 产品ID
   */
  private String productId;

  // 当前订阅的到期时间相关

  /**
   * 自动续订订阅到期或续订的时间，采用类似于 ISO 8601 的日期时间格式。
   */
  private String expiresDate;

  /**
   * 订阅到期的时间
   */
  private Long expiresDateMs;

  private String expiresDatePst;

  /**
   * 一个值，指示用户是产品的购买者还是可以通过家庭共享访问产品的家庭成员。FAMILY_SHARED | PURCHASED
   */
  private Integer inAppOwnershipType;

  /**
   * 自动续订订阅是否处于促销价期的指标. TRUE | FALSE
   */
  private Boolean isInIntroOfferPeriod;

  /**
   * 收取费用的时间
   */
  private Long purchaseDateMs;

  /**
   * 交易的标识符
   */
  private String transactionId;

  /**
   * 原始的应用购买时间。
   *
   * <p>如果是自动续订订阅，表示订阅的初始购买时间</p>
   *
   * <p>对于同一个productId的产品，该时间始终保持不变</p>
   */
  private Long originalPurchaseDateMs;

  /**
   * 如果是第一个订阅，则originTransactionId与transactionId相同。
   *
   * <p>当恢复或者是续订的时候，originTransactionId不transactionId不同</p>
   *
   * <p>对于同一个订阅事件，originTransactionId是相同的，transactionId不同</p>
   */
  private String originalTransactionId;

  /**
   * 表明是否在免费试用期间
   */
  private Boolean isTrialPeriod;

  /**
   * 购买的数量
   */
  private Integer quantity;


}
