package com.impassive.rpc.core.api;

/**
 * @author impassive
 */
public interface Invoker<T> {

  Class<T> getInterface();

}
