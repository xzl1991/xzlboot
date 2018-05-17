package com.utils;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

/**
 * Created by xlizy on 2017/4/23.
 *
 * 日志相关工具类
 */
public class LogUtils {

    /**
     * 获取日志前缀
     * requestUID:请求ID，用于聚合一次请求输出的日志
     * loginName:请求的发起人
     * */
    public static String getPrefix() {
        StringBuilder sb = new StringBuilder();
        String loginName = "<NULL>";
        Assertion a = AssertionHolder.getAssertion();
        if (a != null) {
            AttributePrincipal p = a.getPrincipal();
            if (p != null) {
                loginName = p.getName();
            }
        }

        sb.append("[requestUID=").
                append(ThreadLocals.requestId.get()).
                append(",loginName=").
                append(loginName).
                append("] - ");
        return sb.toString();
    }

    /**
     * 获取日志前缀
     * requestUID:请求ID，用于聚合一次请求输出的日志
     * loginName:请求的发起人
     * */
    public static String getProcessPrefix() {
        StringBuilder sb = new StringBuilder();

        sb.append("[requestUID=").
                append(ThreadLocals.processId.get()).
                append("] - ");
        return sb.toString();
    }

}
