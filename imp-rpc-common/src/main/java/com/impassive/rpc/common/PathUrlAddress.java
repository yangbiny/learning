package com.impassive.rpc.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author impassive
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PathUrlAddress extends UrlAddress {

  private String path;

}
