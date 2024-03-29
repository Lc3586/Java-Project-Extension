package project.extension.httpClient;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project.extension.collections.MapExtension;
import project.extension.exception.CommonException;
import project.extension.tuple.Tuple2;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Http请求工具类
 *
 * @author lctr
 * @date 2022年1月7日
 */
public class HttpClientUtils {
    /**
     * 执行Post请求
     * 数据将以JSON格式发送
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postDataAsJson(String url,
                                                            @Nullable
                                                                    Object data,
                                                            @Nullable
                                                                    HttpHeaders headers) {
        return postDataAsJson(url,
                              data,
                              headers,
                              null,
                              null,
                              null);
    }

    /**
     * 执行Post请求
     * 数据将以JSON格式发送
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postDataAsJson(String url,
                                                            @Nullable
                                                                    Object data,
                                                            @Nullable
                                                                    HttpHeaders headers,
                                                            @Nullable
                                                                    Integer connectionRequestTimeout,
                                                            @Nullable
                                                                    Integer connectTimeout,
                                                            @Nullable
                                                                    Integer readTimeout) {
        String jsonData = JSON.toJSONString(data);
        return postJsonData(url,
                            jsonData,
                            headers,
                            connectionRequestTimeout,
                            connectTimeout,
                            readTimeout);
    }


    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    Json字符串
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postJsonData(String url,
                                                          @Nullable
                                                                  String data,
                                                          @Nullable
                                                                  HttpHeaders headers) {
        return postJsonData(url,
                            data,
                            headers,
                            null,
                            null,
                            null);
    }

    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    Json字符串
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postJsonData(String url,
                                                          @Nullable
                                                                  String data,
                                                          @Nullable
                                                                  HttpHeaders headers,
                                                          @Nullable
                                                                  Integer connectionRequestTimeout,
                                                          @Nullable
                                                                  Integer connectTimeout,
                                                          @Nullable
                                                                  Integer readTimeout) {
        if (headers == null)
            headers = new HttpHeaders();
        if (headers.containsKey("Content-Type"))
            headers.set("Content-Type",
                        "application/json");
        else
            headers.add("Content-Type",
                        "application/json");
        HttpEntity<String> entity = getEntity(data,
                                              headers);
        return postData(url,
                        entity,
                        connectionRequestTimeout,
                        connectTimeout,
                        readTimeout);
    }

    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postObjectData(String url,
                                                            @Nullable
                                                                    Object data,
                                                            @Nullable
                                                                    HttpHeaders headers) {
        return postObjectData(url,
                              data,
                              headers,
                              null,
                              null,
                              null);
    }

    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postObjectData(String url,
                                                            @Nullable
                                                                    Object data,
                                                            @Nullable
                                                                    HttpHeaders headers,
                                                            @Nullable
                                                                    Integer connectionRequestTimeout,
                                                            @Nullable
                                                                    Integer connectTimeout,
                                                            @Nullable
                                                                    Integer readTimeout) {
        Map<String, Object> map;
        try {
            map = data == null
                  ? null
                  : MapExtension.convertObject2Map(data);
        } catch (IllegalAccessException ex) {
            throw new CommonException("对象转map失败",
                                      ex);
        }
        MultiValueMap<String, Object> m_map = data == null
                                              ? null
                                              : new LinkedMultiValueMap<>();
        if (map != null) {
            for (String key : map.keySet())
                m_map.add(key,
                          map.get(key));
        }

        return postData(url,
                        m_map,
                        headers,
                        connectionRequestTimeout,
                        connectTimeout,
                        readTimeout);
    }


    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postData(String url,
                                                      @Nullable
                                                              MultiValueMap<String, Object> data,
                                                      @Nullable
                                                              HttpHeaders headers) {
        return postData(url,
                        data,
                        headers,
                        null,
                        null,
                        null);
    }

    /**
     * 执行Post请求
     *
     * @param url     地址
     * @param data    数据
     * @param headers 头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> postData(String url,
                                                      @Nullable
                                                              MultiValueMap<String, Object> data,
                                                      @Nullable
                                                              HttpHeaders headers,
                                                      @Nullable
                                                              Integer connectionRequestTimeout,
                                                      @Nullable
                                                              Integer connectTimeout,
                                                      @Nullable
                                                              Integer readTimeout) {
        HttpEntity<MultiValueMap<String, Object>> entity = getEntity(data,
                                                                     headers);
        return postData(url,
                        entity,
                        connectionRequestTimeout,
                        connectTimeout,
                        readTimeout);
    }

    /**
     * 获取请求体
     *
     * @param data    数据
     * @param headers 请求头
     * @param <T>     数据类型
     * @return 请求体
     */
    public static <T> HttpEntity<T> getEntity(
            @Nullable
                    T data,
            @Nullable
                    HttpHeaders headers) {
        HttpEntity<T> entity;

        if (headers == null) headers = new HttpHeaders();
        entity = data != null
                 ? new HttpEntity<>(data,
                                    headers)
                 : new HttpEntity<>(headers);

        return entity;
    }

    /**
     * 执行Post请求
     * 数据将以JSON格式发送
     *
     * @param url    地址
     * @param entity 请求体
     * @return (响应状态, 响应数据)
     */
    public static <T> Tuple2<HttpStatus, String> postData(String url,
                                                          @Nullable
                                                                  HttpEntity<T> entity) {
        return postData(url,
                        entity,
                        null,
                        null,
                        null);
    }

    /**
     * 执行Post请求
     * 数据将以JSON格式发送
     *
     * @param url                      地址
     * @param entity                   请求体
     * @param connectionRequestTimeout 请求连接超时时间（ms）
     * @param connectTimeout           连接超时时间（ms）
     * @param readTimeout              读取数据超时时间（ms）
     * @return (响应状态, 响应数据)
     */
    public static <T> Tuple2<HttpStatus, String> postData(String url,
                                                          @Nullable
                                                                  HttpEntity<T> entity,
                                                          @Nullable
                                                                  Integer connectionRequestTimeout,
                                                          @Nullable
                                                                  Integer connectTimeout,
                                                          @Nullable
                                                                  Integer readTimeout) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        if (connectionRequestTimeout == null)
            connectionRequestTimeout = 10000;
        if (connectTimeout == null)
            connectTimeout = 10000;
        if (readTimeout == null)
            readTimeout = 10000;
        httpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        httpRequestFactory.setConnectTimeout(connectTimeout);
        httpRequestFactory.setReadTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        ResponseEntity<String> response = restTemplate.exchange(url,
                                                                HttpMethod.POST,
                                                                entity,
                                                                String.class);
        return new Tuple2<>(response.getStatusCode(),
                            response.getBody());
    }

    /**
     * 执行Get请求
     *
     * @param url     地址
     * @param params  参数键值对集合
     * @param headers 请求头
     */
    public static ResponseEntity<String> doGet(String url,
                                               @Nullable
                                                       Map<String, Object> params,
                                               @Nullable
                                                       MultiValueMap<String, String> headers) {
        return doGet(url,
                     params,
                     headers,
                     null,
                     null,
                     null,
                     null);
    }

    /**
     * 执行Get请求
     *
     * @param url                     地址
     * @param params                  参数键值对集合
     * @param headers                 请求头
     * @param instanceFollowRedirects 是否自动重定向（默认为true）
     */
    public static ResponseEntity<String> doGet(String url,
                                               @Nullable
                                                       Map<String, Object> params,
                                               @Nullable
                                                       MultiValueMap<String, String> headers,
                                               @Nullable
                                                       Boolean instanceFollowRedirects) {
        return doGet(url,
                     params,
                     headers,
                     instanceFollowRedirects,
                     null,
                     null,
                     null);
    }

    /**
     * 执行Get请求
     *
     * @param url                      地址
     * @param params                   参数键值对集合
     * @param headers                  请求头
     * @param instanceFollowRedirects  是否自动重定向（默认为true）
     * @param connectionRequestTimeout 请求连接超时时间（ms）
     * @param connectTimeout           连接超时时间（ms）
     * @param readTimeout              读取数据超时时间（ms）
     */
    public static ResponseEntity<String> doGet(String url,
                                               @Nullable
                                                       Map<String, Object> params,
                                               @Nullable
                                                       MultiValueMap<String, String> headers,
                                               @Nullable
                                                       Boolean instanceFollowRedirects,
                                               @Nullable
                                                       Integer connectionRequestTimeout,
                                               @Nullable
                                                       Integer connectTimeout,
                                               @Nullable
                                                       Integer readTimeout) {
        if (instanceFollowRedirects == null)
            instanceFollowRedirects = true;
        if (connectionRequestTimeout == null)
            connectionRequestTimeout = 10000;
        if (connectTimeout == null)
            connectTimeout = 10000;
        if (readTimeout == null)
            readTimeout = 10000;

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;
        if (instanceFollowRedirects)
            clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        else
            clientHttpRequestFactory = new DisableRedirectClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        HttpHeaders httpHeaders = null;
        if (headers != null)
            httpHeaders = new HttpHeaders(headers);

        HttpEntity<String> entity = getEntity(null,
                                              httpHeaders);

        if (params != null && params.size() > 0) {
            url = String.format("%s?%s",
                                url,
                                params.keySet()
                                      .stream()
                                      .map(x -> String.format("%s={%s}",
                                                              x,
                                                              x))
                                      .collect(Collectors.joining("&")));
        }

        ResponseEntity<String> response;

        if (params != null)
            response = restTemplate.exchange(url,
                                             HttpMethod.GET,
                                             entity,
                                             String.class,
                                             params);
        else
            response = restTemplate.exchange(url,
                                             HttpMethod.GET,
                                             entity,
                                             String.class);

        return response;
    }

    /**
     * 执行Get请求
     *
     * @param url     地址
     * @param params  参数键值对集合
     * @param headers 请求头
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> getData(String url,
                                                     @Nullable
                                                             Map<String, Object> params,
                                                     @Nullable
                                                             MultiValueMap<String, String> headers) {
        return getData(url,
                       params,
                       headers,
                       null,
                       null,
                       null,
                       null);
    }

    /**
     * 执行Get请求
     *
     * @param url                     地址
     * @param params                  参数键值对集合
     * @param headers                 请求头
     * @param instanceFollowRedirects 是否自动重定向（默认为true）
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> getData(String url,
                                                     @Nullable
                                                             Map<String, Object> params,
                                                     @Nullable
                                                             MultiValueMap<String, String> headers,
                                                     @Nullable
                                                             Boolean instanceFollowRedirects) {
        return getData(url,
                       params,
                       headers,
                       null,
                       null,
                       null,
                       null);
    }

    /**
     * 执行Get请求
     *
     * @param url                     地址
     * @param params                  参数键值对集合
     * @param headers                 请求头
     * @param instanceFollowRedirects 是否自动重定向（默认为true）
     * @return (响应状态, 响应数据)
     */
    public static Tuple2<HttpStatus, String> getData(String url,
                                                     @Nullable
                                                             Map<String, Object> params,
                                                     @Nullable
                                                             MultiValueMap<String, String> headers,
                                                     @Nullable
                                                             Boolean instanceFollowRedirects,
                                                     @Nullable
                                                             Integer connectionRequestTimeout,
                                                     @Nullable
                                                             Integer connectTimeout,
                                                     @Nullable
                                                             Integer readTimeout) {
        ResponseEntity<String> response = doGet(url,
                                                params,
                                                headers,
                                                instanceFollowRedirects,
                                                connectionRequestTimeout,
                                                connectTimeout,
                                                readTimeout);

        return new Tuple2<>(response.getStatusCode(),
                            response.getBody());
    }
}
