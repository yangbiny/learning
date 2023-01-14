package com.impassive.pay.result;

/**
 * @author impassive
 */
public record Sign(
    String sign,

    String prepareId,

    String timestamp,

    String nonceStr
) {

}
