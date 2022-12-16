package com.impassive.pay.tools;

import java.util.Map;

/**
 * @author impassive
 */
public record HttpExecuteResult(
    int code,
    Map<String, String> headers,
    String body
) {

}
