package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

/**
 * @author impassive
 */
public interface Invoker<T> {

  ImpUrl url();

  Class<T> getInterface();

}
