package com.impassive.pay.entity.notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IapServiceNotifyV2 {

  private String notificationType;

  private String subtype;

  private String notificationUUID;

  private NotifyV2Data data;

  private String version;

  private Long signedDate;

}
