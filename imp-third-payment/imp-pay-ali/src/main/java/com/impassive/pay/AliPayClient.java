package com.impassive.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;

/**
 * @author impassive
 */
public class AliPayClient {

  private final AliPayProperties aliPayProperties;
  private final DefaultAlipayClient alipayClient;

  public AliPayClient(AliPayProperties aliPayProperties) throws AlipayApiException {
    this.aliPayProperties = aliPayProperties;
    AlipayConfig config = new AlipayConfig();
    config.setServerUrl("https://openapi.alipay.com/gateway.do");
    config.setAlipayPublicKey(aliPayProperties.getAliPublicKey());
    config.setAppId(aliPayProperties.getAliAppId());
    config.setPrivateKey(aliPayProperties.getAliPrivateKey());
    config.setEncryptKey(aliPayProperties.getEncryptKey());
    config.setCharset("UTF-8");
    config.setSignType("RSA2");
    config.setFormat("json");
    this.alipayClient = new DefaultAlipayClient(config);
  }


}
