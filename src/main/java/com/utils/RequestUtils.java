package com.utils;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUtils {
    
    public static final String    HEADER_ACCEPT                        = "Accept";

    public static final String    HEADER_ACCEPT_CHARSET                = "Accept-Charset";

    public static final String    HEADER_ACCEPT_ENCODING                = "Accept-Encoding";

    public static final String    HEADER_ACCEPT_LANGUAGE                = "Accept-Language";

    public static final String    HEADER_CACHE_CONTROL                = "Cache-Control";

    public static final String    HEADER_CONNECTION                    = "Connection";

    public static final String    HEADER_DATE                            = "Date";

    public static final String    HEADER_HOST                            = "Host";

    public static final String    HEADER_REFERER                        = "Referer";

    public static final String    HEADER_USER_AGENT                    = "User-Agent";

    public static final String    HEADER_X_FORWARDED_FOR                = "X-Forwarded-For";

    public static final String    JAVAX_SERVLET_FORWARD_REQUEST_URI    = "javax.servlet.forward.request_uri";

    public static final String    JAVAX_SERVLET_INCLUDE_REQUEST_URI    = "javax.servlet.include.request_uri";

    private static String getServerUrl(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme());
        builder.append("://");
        builder.append(request.getServerName());
        builder.append(":");
        builder.append(request.getServerPort());
        return builder.toString();
    }

    private static String getRequestUri(final HttpServletRequest request) {
        String webInf = request.getContextPath() + "/WEB-INF";
        String uri = request.getRequestURI();
        if (uri.startsWith(webInf)) {
            uri = (String) request.getAttribute(RequestUtils.JAVAX_SERVLET_INCLUDE_REQUEST_URI);
            if (uri.startsWith(webInf)) {
                uri = (String) request.getAttribute(RequestUtils.JAVAX_SERVLET_FORWARD_REQUEST_URI);
            }
        }
        return uri;
    }

    public static String getApplicationUrl(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(RequestUtils.getServerUrl(request));
        builder.append(request.getContextPath());
        return builder.toString();
    }

    public static String getRequestUrl(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(RequestUtils.getServerUrl(request));
        builder.append(RequestUtils.getRequestUri(request));
        return builder.toString();
    }

    public static String getFullRequestUrl(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(RequestUtils.getRequestUrl(request));

        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }

        return builder.toString();
    }

    public static String getRequestPath(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(RequestUtils.getRequestUri(request));
        return builder.toString();
    }

    public static String getFullRequestPath(final HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(RequestUtils.getRequestPath(request));

        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }

        return builder.toString();
    }

    public static String getRequestAddress(final HttpServletRequest request) {
        String address = request.getHeader(RequestUtils.HEADER_X_FORWARDED_FOR);

        if (address != null) {
            if (address.indexOf(',') != -1) {
                address = address.split(",")[0];
            }
            return address;
        }

        return request.getRemoteAddr();
    }

    public static String getMethod(final HttpServletRequest request) {
        return request.getMethod();
    }

    public static String getReferer(final HttpServletRequest request) {
        return request.getHeader(RequestUtils.HEADER_REFERER);
    }

    public static String getUserAgent(final HttpServletRequest request) {
        return request.getHeader(RequestUtils.HEADER_USER_AGENT);
    }

    public static final StringBuilder getErrorInfoFromRequest(HttpServletRequest httpservletrequest,
                                                              boolean flag) {
        StringBuilder stringbuilder = new StringBuilder();
        String s = getErrorUrl(httpservletrequest);
        stringbuilder.append(StringUtils
            .formatMsg("Error processing url: %1, Referrer: %2, Error message: %3.\n",
                       new Object[] {
                           s, httpservletrequest.getHeader("REFERER"),
                           httpservletrequest.getAttribute("javax.servlet.error.message")
                       }));
        Throwable throwable = (Throwable)httpservletrequest.getAttribute("exception");
        if (throwable == null) {
            throwable = (Throwable)httpservletrequest.getAttribute("javax.servlet.error.exception");
        }
        if (throwable != null) {
            stringbuilder.append(StringUtils.formatMsg("Exception stack trace: \n", new Object[] {
                throwable
            }));
        }
        return stringbuilder;
    }

    public static final String getErrorUrl(HttpServletRequest httpservletrequest) {
        String s = (String)httpservletrequest.getAttribute("javax.servlet.error.request_uri");
        if (s == null) {
            s = (String)httpservletrequest.getAttribute("javax.servlet.forward.request_uri");
        }
        if (s == null) {
            s = (String)httpservletrequest.getAttribute("javax.servlet.include.request_uri");
        }
        if (s == null) {
            s = httpservletrequest.getRequestURL().toString();
        }
        return s;
    }

    public static final String getHtml(String s) throws IOException {
        InputStream inputstream;
        StringWriter stringwriter;
        char ac[];
        URL url = new URL(s);
        HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
        inputstream = httpurlconnection.getInputStream();
        stringwriter = new StringWriter();
        if (inputstream == null) {
            throw new IOException();
        }
        ac = new char[1024];
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        int i;
        while ((i = bufferedreader.read(ac)) != -1) {
            stringwriter.write(ac, 0, i);
        }
        inputstream.close();
        return stringwriter.toString();
    }

    public static <T> Map<String,Object> getConditions(T obj){
        return getConditions(obj,null);
    }

    public static <T> Map<String,Object> getConditions(T obj,HttpServletRequest request){
        Map<String,Object> map = new HashMap<String,Object>();
        if (obj != null) {
            try {
                Object temp = null;
                for (Field field : obj.getClass().getDeclaredFields()) {
                    Method method=obj.getClass().getMethod("get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1));
                    temp = method.invoke(obj);
                    if(temp!=null){
                        //map 或数组 集合
                        if((temp instanceof Map)&&(((Map)temp).size()>0)){
                            map.put(field.getName(), temp);
                        }else if( (temp instanceof List)&&(((List)temp).size()>0)){
                            map.put(field.getName(), temp);
                        }else if((temp instanceof Object[])&&((Object[])temp).length>0){
                            map.put(field.getName(), temp);
                        }else if((!(temp instanceof Map))&&!(temp instanceof List)&&!(temp instanceof Object[])&&temp.toString().trim().length()>0){
                            map.put(field.getName(), temp);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(request != null){
            try {
                map.put("limitStart", Integer.valueOf(StringUtils.isBlank(request.getParameter("start"))?"0":request.getParameter("start")));
                map.put("limitEnd", Integer.valueOf(StringUtils.isBlank(request.getParameter("end"))?"25":request.getParameter("end")));
            } catch (NumberFormatException e) {
//            	ExceptionHolder.holder(e);
            	e.printStackTrace();
            }
            try {
                map.put("limitEnd", Integer.valueOf(StringUtils.isBlank(request.getParameter("limit"))?"25":request.getParameter("limit")));
                map.put("sort", request.getParameter("sort"));
                map.put("dir", request.getParameter("dir"));
            } catch (NumberFormatException e) {
//            	ExceptionHolder.holder(e);
                e.printStackTrace();
            }
        }
        // TODO: 2017/3/3 添加排序等参数 参考QueryFilter
        return map;
    }
    /**
     * 从request中获取分配配置
     * */
    public static long[] getPageLimit(HttpServletRequest request){
        long[] limits = new long[]{0,Long.MAX_VALUE};
        try {
            limits[0] = Integer.valueOf(StringUtils.isBlank(request.getParameter("start"))?"0":request.getParameter("start"));
        }catch (Exception e){}
        try {
            limits[1] = Integer.valueOf(request.getParameter("limit"));
        }catch (Exception e){}
        return limits;
    }

    /**
     *
     * 功能描述：获取真实的IP地址
     *
     * @param request
     * @return
     */
    //获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，服务器端应用也无法直接通过转发请求的地址返回给客户端。
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public RequestUtils() {
    }
}
