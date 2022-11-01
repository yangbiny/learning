package com.impassive.rpc.utils;

import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AddressTools {

  public static String getIpAddress() {
    final Enumeration<NetworkInterface> interfaces;
    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
    try {

      while (interfaces.hasMoreElements()) {
        NetworkInterface ip = interfaces.nextElement();
        if (ip.isLoopback() || ip.isVirtual() || !ip.isUp() || ip.isPointToPoint()) {
          continue;
        }
        final Enumeration<InetAddress> addresses = ip.getInetAddresses();
        while (addresses.hasMoreElements()) {
          final InetAddress inetAddress = addresses.nextElement();
          if (!(inetAddress instanceof Inet4Address)) {
            continue;
          }
          return inetAddress.getHostAddress();
        }
      }
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, "while get LocalAddress Error");
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
  }

}
