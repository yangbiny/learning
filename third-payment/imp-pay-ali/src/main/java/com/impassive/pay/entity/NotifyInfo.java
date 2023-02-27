package com.impassive.pay.entity;

import com.impassive.pay.tools.JsonTools;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class NotifyInfo {

  private Map<String, String> params;

  private String body;

  @Nullable
  public static NotifyInfo fromJson(String body) {
    @SuppressWarnings("unchecked")
    HashMap<String, String> map = JsonTools.fromJson(body, HashMap.class);
    if (map == null) {
      return null;
    }
    NotifyInfo notifyInfo = new NotifyInfo();
    notifyInfo.params = map;
    notifyInfo.body = body;
    return notifyInfo;
  }


  public String paymentNo() {
    return this.params.get("out_trade_no");
  }

  public String ourAgreementNo() {
    return this.params.get("external_agreement_no");
  }

}
