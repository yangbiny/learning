package com.impassive.rpc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;

public class JsonTools {

  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
  }

  public static <T> String toJson(T object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
  }

  public static <T> T fromJson(String string, Class<T> tClass) {
    try {
      return mapper.readValue(string, tClass);
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
  }

  public static <T> List<T> fromJsonWithRaw(String string, Class<T> tClass) {
    if (StringTools.isEmpty(string)) {
      return new ArrayList<>(0);
    }
    CollectionType collectionType = mapper.getTypeFactory()
        .constructCollectionType(List.class, tClass);
    try {
      return mapper.readValue(string, collectionType);
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
  }

}
