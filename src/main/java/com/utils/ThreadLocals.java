package com.utils;

/**
 * Created by xlizy on 2017/4/21.
 *
 * 线程私有变量
 */
public class ThreadLocals {

    //区分每一次用户请求，并将该次请求处理过程中的日志串联起来的标识
    public static final ThreadLocal<String> requestId = new ThreadLocal<>();
    //用户请求时的IP地址
    public static final ThreadLocal<String> requestIP = new ThreadLocal<>();

    //非用户发起的请求线程的串联标识
    public static final ThreadLocal<String> processId = new ThreadLocal<>();

}
