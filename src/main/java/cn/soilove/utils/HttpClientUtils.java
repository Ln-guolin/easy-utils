package cn.soilove.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClients请求工具类
 *
 * @author: Chen GuoLin
 * @create: 2021-01-20 15:45
 **/
@Slf4j
public class HttpClientUtils {

    /**
     * 默认请求配置
     */
    private static final RequestConfig DEF_REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(10000)
            .setConnectionRequestTimeout(10000)
            .setSocketTimeout(10000)
            .setRedirectsEnabled(true)
            .build();;

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @return
     */
    public static String httpPost(String uri, Map<String,String> paramsMap){
        // 发起请求
        return httpPost(uri,paramsMap,null,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static String httpPost(String uri, Map<String,String> paramsMap,RequestConfig requestConfig){
        // 发起请求
        return httpPost(uri,paramsMap,requestConfig,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpPost(String uri, Map<String,String> paramsMap,Map<String,String> headersMap){
        // 发起请求
        return httpPost(uri,paramsMap,null,headersMap);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpPost(String uri, Map<String,String> paramsMap, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        // 添加请求配置
        httpPost.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpPost.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 参数设置
            if(MapUtils.isNotEmpty(paramsMap)){
                // 封装参数
                List<NameValuePair> nameValuePair = new ArrayList();
                paramsMap.forEach((k,v) -> nameValuePair.add(new BasicNameValuePair(k, v)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, Charset.forName("UTF-8")));
            }

            // 请求
            response = httpclient.execute(httpPost);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            log.error("[httpPost]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpPost]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpPost]response.close() IOException!",e);
                }
            }
        }
        return null;
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @return
     */
    public static String httpPost(String uri, String jsonParam){
        // 发起请求
        return httpPost(uri,jsonParam,null,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static String httpPost(String uri, String jsonParam,RequestConfig requestConfig){
        // 发起请求
        return httpPost(uri,jsonParam,requestConfig,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpPost(String uri, String jsonParam,Map<String,String> headersMap){
        // 发起请求
        return httpPost(uri,jsonParam,null,headersMap);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpPost(String uri, String jsonParam, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        // 添加请求配置
        httpPost.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpPost.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 参数设置
            if(StringUtils.isNotBlank(jsonParam)){
                StringEntity s = new StringEntity(jsonParam, Charset.forName("UTF-8"));
                s.setContentEncoding("UTF-8");
                httpPost.setEntity(s);
            }

            // 请求
            response = httpclient.execute(httpPost);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            log.error("[httpPost]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpPost]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpPost]response.close() IOException!",e);
                }
            }
        }
        return null;
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @return
     */
    public static byte[] httpPost4byte(String uri, Map<String,String> paramsMap){
        // 发起请求
        return httpPost4byte(uri,paramsMap,null,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static byte[] httpPost4byte(String uri, Map<String,String> paramsMap,RequestConfig requestConfig){
        // 发起请求
        return httpPost4byte(uri,paramsMap,requestConfig,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpPost4byte(String uri, Map<String,String> paramsMap,Map<String,String> headersMap){
        // 发起请求
        return httpPost4byte(uri,paramsMap,null,headersMap);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpPost4byte(String uri, Map<String,String> paramsMap, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        // 添加请求配置
        httpPost.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpPost.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 参数设置
            if(MapUtils.isNotEmpty(paramsMap)){
                // 封装参数
                List<NameValuePair> nameValuePair = new ArrayList();
                paramsMap.forEach((k,v) -> nameValuePair.add(new BasicNameValuePair(k, v)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, Charset.forName("UTF-8")));
            }

            // 请求
            response = httpclient.execute(httpPost);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            }
            log.error("[httpPost4byte]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpPost4byte]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpPost4byte]response.close() IOException!",e);
                }
            }
        }
        return null;
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @return
     */
    public static byte[] httpPost4byte(String uri, String jsonParam){
        // 发起请求
        return httpPost4byte(uri,jsonParam,null,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static byte[] httpPost4byte(String uri, String jsonParam,RequestConfig requestConfig){
        // 发起请求
        return httpPost4byte(uri,jsonParam,requestConfig,null);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpPost4byte(String uri, String jsonParam,Map<String,String> headersMap){
        // 发起请求
        return httpPost4byte(uri,jsonParam,null,headersMap);
    }

    /**
     * post 请求
     * @param uri 请求地址
     * @param jsonParam 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpPost4byte(String uri, String jsonParam, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        // 添加请求配置
        httpPost.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpPost.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 参数设置
            if(StringUtils.isNotBlank(jsonParam)){
                StringEntity s = new StringEntity(jsonParam, Charset.forName("UTF-8"));
                s.setContentEncoding("UTF-8");
                httpPost.setEntity(s);
            }

            // 请求
            response = httpclient.execute(httpPost);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            }
            log.error("[httpPost4byte]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpPost4byte]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpPost4byte]response.close() IOException!",e);
                }
            }
        }
        return null;
    }

    /**
     * get 请求
     * @param url 请求地址
     * @return
     */
    public static String httpGet(String url){
        // 发起请求
        return httpGet(url,null,null,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @return
     */
    public static String httpGet(String uri,RequestConfig requestConfig){
        // 发起请求
        return httpGet(uri,null,requestConfig,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @return
     */
    public static String httpGet(String uri, Map<String,String> paramsMap){
        // 发起请求
        return httpGet(uri,paramsMap,null,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static String httpGet(String uri, Map<String,String> paramsMap,RequestConfig requestConfig){
        // 发起请求
        return httpGet(uri,paramsMap,requestConfig,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpGet(String uri, Map<String,String> paramsMap,Map<String,String> headersMap){
        // 发起请求
        return httpGet(uri,paramsMap,null,headersMap);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static String httpGet(String uri, Map<String,String> paramsMap, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 参数封装
        StringBuffer uriBuffer = buildHttpGetUri(uri, paramsMap);

        HttpGet httpGet = new HttpGet(uriBuffer.toString());

        // 添加请求配置
        httpGet.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpGet.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 请求
            response = httpclient.execute(httpGet);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            log.error("[httpGet]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpGet]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpGet]response.close() IOException!",e);
                }
            }
        }
        return null;
    }

    /**
     * get 请求
     * @param url 请求地址
     * @return
     */
    public static byte[] httpGet4byte(String url){
        // 发起请求
        return httpGet4byte(url,null,null,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @return
     */
    public static byte[] httpGet4byte(String uri,RequestConfig requestConfig){
        // 发起请求
        return httpGet4byte(uri,null,requestConfig,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @return
     */
    public static byte[] httpGet4byte(String uri, Map<String,String> paramsMap){
        // 发起请求
        return httpGet4byte(uri,paramsMap,null,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置
     * @return
     */
    public static byte[] httpGet4byte(String uri, Map<String,String> paramsMap,RequestConfig requestConfig){
        // 发起请求
        return httpGet4byte(uri,paramsMap,requestConfig,null);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpGet4byte(String uri, Map<String,String> paramsMap,Map<String,String> headersMap){
        // 发起请求
        return httpGet4byte(uri,paramsMap,null,headersMap);
    }

    /**
     * get 请求
     * @param uri 请求地址
     * @param paramsMap 请求参数
     * @param requestConfig 请求配置，如：RequestConfig.custom().setConnectTimeout(10000).build()
     * @param headersMap 请求头信息
     * @return
     */
    public static byte[] httpGet4byte(String uri, Map<String,String> paramsMap, RequestConfig requestConfig,Map<String,String> headersMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 参数封装
        StringBuffer uriBuffer = buildHttpGetUri(uri, paramsMap);

        HttpGet httpGet = new HttpGet(uriBuffer.toString());

        // 添加请求配置
        httpGet.setConfig(requestConfig != null ? requestConfig : DEF_REQUEST_CONFIG);

        // 添加头部信息
        if(MapUtils.isNotEmpty(headersMap)){
            headersMap.forEach((k,v) -> httpGet.setHeader(k,v));
        }

        CloseableHttpResponse response = null;
        try {
            // 请求
            response = httpclient.execute(httpGet);

            // 判断请求状态码
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toByteArray(response.getEntity());
            }
            log.error("[httpGet4byte]request Error!",response.getStatusLine().toString());
            return null;
        } catch (IOException e) {
            log.error("[httpGet4byte]request IOException!",e);
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("[httpGet4byte]response.close() IOException!",e);
                }
            }
        }
        return null;
    }


    /**
     * get 参数封装
     * @param uri
     * @param paramsMap
     * @return
     */
    private static StringBuffer buildHttpGetUri(String uri, Map<String, String> paramsMap) {
        StringBuffer uriBuffer = new StringBuffer(uri);
        if (MapUtils.isNotEmpty(paramsMap)) {
            paramsMap.forEach((k, v) -> {
                try {
                    v = URLEncoder.encode(v, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.error("[buildHttpGetUri]URLEncoder.encode Error! k=" + k + ",v=" + v, e);
                }
                // uri存在参数，追加，uri不存在参数，直接封装
                if (uri.contains("?")) {
                    uriBuffer.append("&");
                } else {
                    uriBuffer.append("?");
                }
                uriBuffer.append(k).append("=").append(v);
            });
        }
        return uriBuffer;
    }
}
