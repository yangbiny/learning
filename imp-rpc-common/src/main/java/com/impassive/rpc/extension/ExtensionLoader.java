package com.impassive.rpc.extension;

import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ExtensionException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展点加载器
 */
public class ExtensionLoader<T> {

  private static final String DEFAULT_KEY = "default";

  private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();

  private final Map<String, T> extensionMap = new ConcurrentHashMap<>();

  private final T extensionType;

  private ExtensionLoader(T extensionType) {
    this.extensionType = extensionType;
  }

  /**
   * 构建加载器。如果 {@code extensionType} 等于null，则会抛出异常。
   *
   * @param extensionType 需要构建加载器的类型
   * @param <T> 加载器的类型
   * @return 构建的加载器
   */
  @SuppressWarnings("unchecked")
  public static <T> ExtensionLoader<T> buildExtensionLoader(Class<T> extensionType) {
    if (extensionType == null) {
      throw new ExtensionException(ExceptionCode.EXTENSION_EXCEPTION, "扩展点类型不能为null");
    }
    ExtensionLoader<?> extensionLoader = EXTENSION_LOADER_MAP.get(extensionType);
    if (extensionLoader == null) {
      extensionLoader = createExtensionLoader(extensionType);
      EXTENSION_LOADER_MAP.put(extensionType, extensionLoader);
    }
    return (ExtensionLoader<T>) extensionLoader;
  }

  private static <T> ExtensionLoader<?> createExtensionLoader(Class<T> extensionType) {
    if (extensionType == null) {
      throw new ExtensionException(ExceptionCode.EXTENSION_EXCEPTION, "扩展点类型不能为null");
    }
    return new ExtensionLoader<>(extensionType);
  }

  /**
   * 构建默认的 扩展点。
   *
   * <p>该扩展点加载 需要 指定 class 的 完全限定名 对应的 文件中存在 default 的key，如果不存在，则会抛出异常</p>
   *
   * @return 默认的扩展点实例对象
   */
  public T buildDefaultExtension() {
    T extensionNode = extensionMap.get(DEFAULT_KEY);
    if (extensionNode != null) {
      return extensionNode;
    }
    extensionNode = initExtensionNodes(DEFAULT_KEY);
    extensionMap.put(DEFAULT_KEY, extensionNode);
    return extensionNode;
  }

  private T initExtensionNodes(String extensionKey) {
    ClassLoader classLoader = extensionType.getClass().getClassLoader();
    String extensionFileName = extensionType.toString();
    try {
      Enumeration<URL> resources = classLoader.getResources(extensionFileName);
      Iterator<URL> urlIterator = resources.asIterator();
      while (urlIterator.hasNext()) {
        URL next = urlIterator.next();
      }
    } catch (IOException e) {
      throw new ExtensionException(ExceptionCode.EXTENSION_FILE_LOAD_EXCEPTION, "扩展点加载失败", e);
    }
    return null;
  }


}
