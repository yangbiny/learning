package com.impassive.pay.entity.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impassive.pay.entity.notify.v1.ReceiptData;
import lombok.Data;

/**
 * @author impassive
 */
@Data
public class IapServiceNotifyV1 {

  private String bid;
  private String password;
  private String bvrs;
  @JsonProperty("original_transaction_id")
  private String originalTransactionId;
  @JsonProperty("auto_renew_adam_id")
  private String autoRenewAdamId;
  @JsonProperty("auto_renew_status")
  private Boolean autoRenewStatus;
  private String environment;
  @JsonProperty("notification_type")
  private String notificationType;
  @JsonProperty("auto_renew_status_change_date")
  private String autoRenewStatusChangeDate;
  @JsonProperty("auto_renew_status_change_date_ms")
  private Long autoRenewStatusChangeDateMs;
  @JsonProperty("auto_renew_status_change_date_pst")
  private String autoRenewStatusChangeDatePst;
  @JsonProperty("auto_renew_product_id")
  private String autoRenewProductId;
  @JsonProperty("unified_receipt")
  private ReceiptData unifiedReceipt;
  @JsonProperty("expiration_intent")
  private Integer expirationIntent;

}
