package com.impassive.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.impassive.pay.cmd.CreatePaySignCmd;
import com.impassive.pay.exception.ApplySignException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassive
 */
@Slf4j
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

  public String applyPaymentSign(CreatePaySignCmd createPaySignCmd) {
    if (createPaySignCmd == null || createPaySignCmd.checkIsIllegal()) {
      throw new IllegalArgumentException("apply sign param is illegal");
    }
    try {
      AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
      AlipayTradeAppPayModel model = buildOnceRequest(createPaySignCmd);
      request.setBizModel(model);
      request.setNotifyUrl(aliPayProperties.getPayNotifyUrl());
      request.setNeedEncrypt(false);
      AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
      if (!response.isSuccess()) {
        log.error("请求 签名失败 ： {},{}", response, createPaySignCmd);
        throw new ApplySignException("apply ali pay sign failed");
      }
      return response.getBody();
    } catch (Exception e) {
      log.error("AliPay create trade error, ", e);
      throw new ApplySignException(e);
    }
  }



  /* ========================================= */

  private AlipayTradeAppPayModel buildOnceRequest(CreatePaySignCmd createPaySignCmd) {
    AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    // 订单标题
    model.setSubject(createPaySignCmd.getInventoryName());
    // 订单描述
    // 订单 id -----> trade_no	支付成功后才有这个流水号
    model.setOutTradeNo(createPaySignCmd.getPaymentNo());
    // 订单相对超时时间。无线支付场景最小值为5m，低于5m支付超时时间按5m计算。 具体参考文档
    model.setTimeoutExpress(aliPayProperties.getOrderTimeOutExpress().toMinutes() + "m");
    // 注意单位为元，精确到小数点后两位，取值范围：[0.01,100000000]
    model.setTotalAmount(toAliAmount(createPaySignCmd.getAmount().asFen()));
    // 商品类型
    model.setProductCode(createPaySignCmd.getProductCode().getValue());
    model.setSellerId(aliPayProperties.getSellerId());
    return model;
  }


  private String toAliAmount(Integer amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount can not be negative " + amount);
    }
    return Double.valueOf(amount / 100.0).toString();
  }

}
