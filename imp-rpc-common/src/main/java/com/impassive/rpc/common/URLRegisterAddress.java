package com.impassive.rpc.common;

import lombok.Getter;

/**
 * 注册中心的信息
 */
@Getter
public record URLRegisterAddress(String address, Integer port, String path) {

}
