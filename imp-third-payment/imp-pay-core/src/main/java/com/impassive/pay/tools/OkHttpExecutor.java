package com.impassive.pay.tools;

import com.impassive.pay.exception.HttpException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

/**
 * @author impassive
 */
public class OkHttpExecutor {

  private final OkHttpClient httpClient;

  public OkHttpExecutor(OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }


  public HttpExecuteResult executeWithPost(String url, String body, MediaType mediaType) {
    Request request = new Builder()
        .url(url)
        .post(RequestBody.create(
            body,
            mediaType
        )).build();

    return buildResult(request);
  }

  public HttpExecuteResult executeWithGet(String url) {
    Request request = new Builder()
        .url(url)
        .get()
        .build();

    return buildResult(request);
  }

  @NotNull
  private HttpExecuteResult buildResult(Request request) {
    try {
      Response execute = httpClient.newCall(request).execute();
      int code = execute.code();
      ResponseBody responseBody = execute.body();
      String responseBodyStr = "";
      if (responseBody != null) {
        responseBodyStr = responseBody.string();
      }
      Headers headers = execute.headers();
      Map<String, String> headMap = new HashMap<>();
      if (headers.size() > 0) {
        Iterator<Pair<String, String>> iterator = headers.iterator();
        while (iterator.hasNext()) {
          Pair<String, String> next = iterator.next();
          headMap.put(next.getFirst(), next.getSecond());
        }
      }
      return new HttpExecuteResult(code, headMap, responseBodyStr);
    } catch (Exception e) {
      throw new HttpException("执行http请求失败", e);
    }
  }

}
