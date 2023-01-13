package com.impassive.pay.result;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Data;

/**
 * @author impassive
 */
@Data
public class AliSubResult {

  /**
   * 用户签约成功后的协议号
   */
  @ApiField("agreement_no")
  private String agreementNo;

  /**
   * 返回脱敏的支付宝账号
   */
  @ApiField("alipay_logon_id")
  private String alipayLogonId;

  /**
   * 代扣协议中标示用户的唯一签约号(确保在商户系统中唯一)
   */
  @ApiField("external_agreement_no")
  private String externalAgreementNo;

  /**
   * 外部登录Id
   */
  @ApiField("external_logon_id")
  private String externalLogonId;

  /**
   * 协议失效时间，格式为 yyyy-MM-dd HH:mm:ss。
   */
  @ApiField("invalid_time")
  private String invalidTime;

  /**
   * 周期扣协议，上次扣款成功时间
   */
  @ApiField("last_deduct_time")
  private String lastDeductTime;

  /**
   * 周期扣协议，预计下次扣款时间
   */
  @ApiField("next_deduct_time")
  private String nextDeductTime;

  /**
   * 协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。
   */
  @ApiField("personal_product_code")
  private String personalProductCode;

  /**
   * 签约主体类型。 CARD:支付宝账号 CUSTOMER:支付宝用户
   */
  @ApiField("pricipal_type")
  private String pricipalType;

  /**
   * 签约主体标识。 当principal_type为CARD 时，该字段为支付宝用户号; 当principal_type为 CUSTOMER 时，该字段为支付宝用户标识。一个用户
   * 可能有多个支付宝账号，即多个支付宝用户号，但只有一个是支付宝用户标识。 一个支付宝账号对应一个支付宝唯一用户号(以2088开头的16位纯数字组成)。
   */
  @ApiField("principal_id")
  private String principalId;

  /**
   * 签约主体标识。 当principal_type为CARD 时，该字段为支付宝用户号; 当principal_type为 CUSTOMER 时，该字段为支付宝用户标识。一个用户
   * 可能有多个支付宝账号，即多个支付宝用户号，但只有一个是支付宝用户标识。
   */
  @ApiField("principal_open_id")
  private String principalOpenId;

  /**
   * 签约协议的场景。
   */
  @ApiField("sign_scene")
  private String signScene;

  /**
   * 协议签约时间，格式为 yyyy-MM-dd HH:mm:ss。
   */
  @ApiField("sign_time")
  private String signTime;

  /**
   * 单笔代扣额度
   */
  @ApiField("single_quota")
  private String singleQuota;

  /**
   * 协议当前状态 1. TEMP：暂存，协议未生效过； 2. NORMAL：正常； 3. STOP：暂停
   */
  @ApiField("status")
  private String status;

  /**
   * 签约第三方主体类型。对于三方协议，表示当前用户和哪一类的第三方主体进行签约。 取值范围： 1. PARTNER（平台商户）; 2.
   * MERCHANT（集团商户），集团下子商户可共享用户签约内容; 默认为PARTNER。
   */
  @ApiField("third_party_type")
  private String thirdPartyType;

  /**
   * 协议生效时间，格式为 yyyy-MM-dd HH:mm:ss。
   */
  @ApiField("valid_time")
  private String validTime;

}
