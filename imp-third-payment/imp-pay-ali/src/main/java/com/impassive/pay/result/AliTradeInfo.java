package com.impassive.pay.result;

import com.impassive.pay.entity.AliTradeStatus;
import java.util.Date;
import lombok.Data;

/**
 * @author impassive
 */
@Data
public class AliTradeInfo {

  private String alipayStoreId;

  /**
   * 间连商户在支付宝端的商户编号； 只在银行间联交易场景下返回该信息；
   */
  private String alipaySubMerchantId;

  /**
   * 预授权支付模式，该参数仅在信用预授权支付场景下返回。信用预授权支付：CREDIT_PREAUTH_PAY
   */
  private String authTradePayMode;

  /**
   * 订单描述； 只在银行间联交易场景下返回该信息；
   */
  private String body;

  /**
   * 买家支付宝账号
   */
  private String buyerLogonId;

  /**
   * 买家支付宝用户唯一标识
   */
  private String buyerOpenId;

  /**
   * 买家实付金额，单位为元，两位小数。该金额代表该笔交易买家实际支付的金额，不包含商户折扣等金额
   */
  private String buyerPayAmount;

  /**
   * 买家在支付宝的用户id
   */
  private String buyerUserId;

  /**
   * 买家名称； 买家为个人用户时为买家姓名，买家为企业用户时为企业名称； 默认不返回该信息，需与支付宝约定后配置返回；
   */
  private String buyerUserName;

  /**
   * 买家用户类型。CORPORATE:企业用户；PRIVATE:个人用户。
   */
  private String buyerUserType;

  /**
   * 该笔交易针对收款方的收费金额； 只在银行间联交易场景下返回该信息；
   */
  private String chargeAmount;


  /**
   * 商家订单号
   */
  private String outTradeNo;


  /**
   * 支付币种订单金额
   */
  private String payAmount;

  /**
   * 订单支付币种
   */
  private String payCurrency;

  /**
   * 积分支付的金额，单位为元，两位小数。该金额代表该笔交易中用户使用积分支付的金额，比如集分宝或者支付宝实时优惠等
   */
  private String pointAmount;

  /**
   * 实收金额，单位为元，两位小数。该金额为本笔交易，商户账户能够实际收到的金额
   */
  private String receiptAmount;

  /**
   * 收款资金类型，当交易收款资金为数字人民币时返回值为“DC”，否则不返回该字段。
   */
  private String receiptCurrencyType;

  /**
   * 本次交易打款给卖家的时间
   */
  private Date sendPayDate;

  /**
   * 结算币种订单金额
   */
  private String settleAmount;

  /**
   * 订单结算币种，对应支付接口传入的settle_currency，支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、南非兰特：ZAR
   */
  private String settleCurrency;


  /**
   * 商户门店编号
   */
  private String storeId;

  /**
   * 请求交易支付中的商户店铺的名称
   */
  private String storeName;

  /**
   * 订单标题； 只在银行间联交易场景下返回该信息；
   */
  private String subject;

  /**
   * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount
   */
  private String totalAmount;

  /**
   * 支付宝交易号
   */
  private String tradeNo;

  /**
   * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
   */
  private AliTradeStatus tradeStatus;


}
