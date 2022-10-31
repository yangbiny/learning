package com.impassive.rpc.extension;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ExtensionException;
import com.impassive.rpc.utils.StringTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

  private final Map<String, String> keyPathMap = new ConcurrentHashMap<>();

  private final Class<T> extensionType;

  private ExtensionLoader(Class<T> extensionType) {
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

    String path = keyPathMap.get(extensionKey);
    if (StringTools.isNotEmpty(path)) {
      return initInstance(path);
    }

    String extensionFileName = "imp-rpc/" + extensionType.getName();
    try {
      Enumeration<URL> resources = ClassLoader.getSystemResources(extensionFileName);
      Iterator<URL> urlIterator = resources.asIterator();
      while (urlIterator.hasNext()) {
        URL next = urlIterator.next();
        buildResource(next);
      }
    } catch (IOException e) {
      throw new ExtensionException(ExceptionCode.EXTENSION_FILE_LOAD_EXCEPTION, "扩展点加载失败", e);
    }
    path = keyPathMap.get(extensionKey);
    if (StringTools.isEmpty(path)) {
      throw new ExtensionException(ExceptionCode.EXTENSION_EXCEPTION,
          String.format("扩展点不存在：key = %s", extensionKey));
    }
    return initInstance(path);
  }

  @SuppressWarnings("unchecked")
  private T initInstance(String classPath) {
    try {
      Class<?> aClass = Class.forName(classPath);
      return (T) aClass.getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ExtensionException(ExceptionCode.EXTENSION_EXCEPTION, "找不到对应的类", e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      throw new ExtensionException(ExceptionCode.EXTENSION_EXCEPTION, "实例化对象失败", e);
    }
  }

  private void buildResource(URL url) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] split = line.split("=");
        if (split.length != 2) {
          throw new ExtensionException(ExceptionCode.EXTENSION_CONFIG_EXCEPTION,
              String.format("扩展点配置不合法 ：path = %s，content = %s", url.getPath(), line));
        }
        keyPathMap.put(split[0], split[1]);
      }
    } catch (IOException e) {
      throw new ExtensionException(ExceptionCode.EXTENSION_FILE_LOAD_EXCEPTION, "读取文件失败", e);
    }
  }


  public T buildExtension(ImpUrl<?> impUrl) {
    return null;
  }
}
