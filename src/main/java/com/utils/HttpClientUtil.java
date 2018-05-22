package com.utils;

import com.holders.ExceptionHolder;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HttpClientUtil implements DisposableBean {

    static final int timeOut = 40 * 1000;

    private static Map<URI, CloseableHttpClient> cache = new ConcurrentHashMap<>();

    private static Map<URI, CloseableHttpClient> caches = new ConcurrentHashMap<>();

	private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeOut)
            .setConnectTimeout(timeOut)
            .setConnectionRequestTimeout(timeOut)
            .build();

    private static HttpClientUtil instance = null;
    private HttpClientUtil(){}
    public static HttpClientUtil getInstance(){
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }

    /**
     * 创建HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient createHttpClient(int maxTotal,
                                                       int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    /**
     * 获取HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient getHttpClient(URI uri) {
        if (!cache.containsKey(uri)) {
            cache.put(uri, createHttpClient(200, 40, 100, uri.getHost(), uri.getPort() == -1 ? 80 : uri.getPort()));
        }
        return cache.get(uri);
    }

    /**
     * 获取httpsclient对象
     *
     * @param uri
     * @return
     * @throws IOException
     */
    public static CloseableHttpClient getHttpsClient(URI uri) throws IOException {
        if (!caches.containsKey(uri)) {
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(uri.toURL());
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            caches.put(uri, HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build());
        }
        return caches.get(uri);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params) throws Exception{

        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求  带权限验证
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPostJson(String httpUrl, String params,String loginName,String passWord) throws Exception{
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            String encoding = DatatypeConverter.printBase64Binary((loginName+":"+passWord).getBytes("UTF-8"));
            httpPost.setHeader("Authorization", "Basic "+encoding);
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPostJson(String httpUrl, String params) throws Exception{

        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param maps 参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            ExceptionHolder.holder(e);
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpPost.setConfig(requestConfig);
            response = getHttpClient(httpPost.getURI()).execute(httpPost,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            ExceptionHolder.holder(e);
        } finally {
            // 关闭连接,释放资源
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                ExceptionHolder.holder(e);
            }
        }
        return result;
    }  
  
    /** 
     * 发送 get请求 
     * @param httpUrl 
     */  
    public String sendHttpGet(String httpUrl) {  
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求  
        return sendHttpGet(httpGet);  
    }

    /**
     * 发送 get请求
     * @param httpUrl 地址
     * @param maps 参数
     */
    public String sendHttpGet(String httpUrl, Map<String, String> maps) throws Exception {
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        HttpGet httpGet = new HttpGet(httpUrl+"?"+ EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8)));// 创建get请求
        return sendHttpsGet(httpGet);
    }
    /** 
     * 发送 get请求Https 
     * @param httpUrl 
     */  
    public String sendHttpsGet(String httpUrl) {  
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求  
        return sendHttpsGet(httpGet);  
    }  
      
    /** 
     * 发送Get请求 
     * @param httpGet
     * @return 
     */  
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            httpGet.setConfig(requestConfig);
            // 执行请求  
            response = getHttpClient(httpGet.getURI()).execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {  
            ExceptionHolder.holder(e);  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }
            } catch (IOException e) {  
                ExceptionHolder.holder(e);  
            }  
        }  
        return responseContent;  
    }  
      
    /** 
     * 发送Get请求Https 
     * @param httpGet
     * @return 
     */  
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpResponse response = null;
        String responseContent = null;  
        try {
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = getHttpsClient(httpGet.getURI()).execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {  
            ExceptionHolder.holder(e);  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }
            } catch (IOException e) {  
                ExceptionHolder.holder(e);  
            }  
        }  
        return responseContent;  
    }

    @Override
    public void destroy() throws Exception {
        cache.putAll(caches);
        for(CloseableHttpClient client : cache.values()) {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    ExceptionHolder.holder(e);
                }
            }
        }
    }
}












