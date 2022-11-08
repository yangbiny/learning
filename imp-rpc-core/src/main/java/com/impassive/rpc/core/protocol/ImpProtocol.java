package com.impassive.rpc.core.protocol;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Invoker;
import com.impassive.rpc.core.api.Protocol;

public class ImpProtocol implements Protocol {


  @Override
  public void export(Invoker<?> invoker) {

  }

  @Override
  public <T> T refer(Class<?> classType, ImpUrl refer) {
    return null;
  }

  @Override
  public void unExport() {

  }
}
