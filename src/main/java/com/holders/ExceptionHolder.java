package com.holders;

import com.alibaba.fastjson.JSONObject;
import com.utils.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by xlizy on 2017/4/18.
 *
 * 错误处理类
 */
@Slf4j
public class ExceptionHolder {

    private static FirstHandler firstHandler;

    /**
     * 初始化责任链
     * */
    static {
        //初始化第一以及默认处理类
        firstHandler = new FirstHandler();
        //初始化后续处理类(需要特殊处理的)
        SQLExceptionHandler sqlExceptionHandler = new SQLExceptionHandler();
        DruidExceptionHandler druidExceptionHandler = new DruidExceptionHandler();
        NetExceptionHandler netExceptionHandler = new NetExceptionHandler();
        TransactionExceptionHandler transactionExceptionHandler = new TransactionExceptionHandler();

        //配置链接
        firstHandler.setNextHandler(sqlExceptionHandler);
        sqlExceptionHandler.setNextHandler(druidExceptionHandler);
        druidExceptionHandler.setNextHandler(netExceptionHandler);
        netExceptionHandler.setNextHandler(transactionExceptionHandler);
    }

    /**
     * 打印日志
     * */
    private static <T extends Exception>  void printLog(JSONObject j, T e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        j.put("HandleFinished",Boolean.TRUE);
        j.put("ExceptionStackTraceInfo",sw.toString().replaceAll("\n","||").replaceAll("\r","||"));
        j.put("ExceptionStackTraceInfoForMail",sw.toString());
        j.put("ExceptionMsg",e.getMessage());
        j.put("ExceptionType",e.getClass().getName());
        log.error("{}The application generates an exception -> {ExceptionType:{},ExceptionMsg:{},ExceptionStackTraceInfo:{},GCInfo:{}}",
                j.get("LogPrefix"),j.get("ExceptionType"),j.get("ExceptionMsg"),j.get("ExceptionStackTraceInfo"),j.get("GCInfo"));
    }

    /**
     * 发送报警邮件
     * */
    private static void sendMail(JSONObject j){
        EMailUtils eMailUtils = SpringContextHolder.getBean(EMailUtils.class);
        if ("true".equals(eMailUtils.getEnabled())) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("LogPrefix:").append(j.getString("LogPrefix")).
                append("ExceptionType:").append(j.getString("ExceptionType")).append("\n")
                        .append("ExceptionMsg:").append(j.getString("ExceptionMsg")).append("\n")
                        .append("ExceptionStackTraceInfo:").append(j.getString("ExceptionStackTraceInfoForMail")).append("\n")
                        .append("jmapInfo:\n").append(j.getString("jmapInfo"));
                JSONObject mail = new JSONObject();
                mail.put("Subject","【WEB发生异常】- "+ SystemInfo.getLocalIP());
                mail.put("Msg",sb.toString());
                mail.put("To",eMailUtils.getGroupOLG());
                eMailUtils.sendSimpleTextEmail(mail);
            } catch (EmailException e) {
                printLog(j,e);
            }
        }
    }

    /**
     * 判断异常是不是继承自Error
     * */
    private static <T extends Exception> boolean extendsByError(T ex){
        if(ex == null)
            return false;

        Class e = ex.getCause()!=null?ex.getCause().getClass():ex.getClass();
        while (e.getSuperclass() != null){
            e = e.getSuperclass();
            if(Error.class.getSimpleName().equals(e.getSimpleName()))
                return true;
        }
        return false;
    }

    /**
     * 异常处理方法
     * */
    public static <T extends Exception> void holder(final T ex){
        final String prefix = LogUtils.getPrefix();
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("LogPrefix",prefix);
        ThreadPools.mailPool.execute(new Runnable() {
            @Override
            public void run() {
                if(extendsByError(ex)){
                    //发生Error异常通常不是程序逻辑错误，所以记录一下内存等使用情况
                    StringBuilder systemInfo = new StringBuilder();
                    InputStream is = null;
                    try {
                        Process process = Runtime.getRuntime().exec("jmap -heap "+ PIDUtil.getPID());
                        is = process.getInputStream();
                        byte[] b = new byte[1024];
                        int len;
                        while ((len = is.read(b))!=-1){
                            systemInfo.append(new String(b,0,len));
                        }
                        jsonObject.put("jmapInfo",systemInfo.toString());
                    } catch (IOException e) {
                        printLog(jsonObject,ex);
                    }finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                    printLog(jsonObject,ex);
                    sendMail(jsonObject);
                }else{
                    jsonObject.put("HandleFinished",Boolean.FALSE);
                    firstHandler.holder(jsonObject,ex);
                    //责任链过完后HandleFinished=false表示该异常不需要特殊处理则打印一下日志就好了
                    if(!jsonObject.getBoolean("HandleFinished")){
                        if(ex != null){
                            printLog(jsonObject,ex);
                        }
                    }
                }
            }
        });
    }

    /**
     * 异常处理抽象类
     * */
    abstract static class Handler{

        @Getter @Setter
        private Handler nextHandler;

        abstract <T extends Exception> void holder(JSONObject j, T e);
    }


    //占位用
    private static class FirstHandler extends Handler{
        @Override
        public void holder(JSONObject j, Exception e) {
            Handler handler = getNextHandler();
            if(handler != null){
                getNextHandler().holder(j,e);
            }
        }
    }

    /**
     * 获取异常类的包名
     * */
    private static <T extends Exception>  String getPackageName(T e){
        String className = e.getClass().getName();
        String packageName = className.substring(0,className.lastIndexOf("."));
        return packageName;
    }


    /**
     * 数据库连接池异常处理
     * */
    private static class DruidExceptionHandler extends Handler{

        @Override
        <T extends Exception> void holder(JSONObject j, T e) {

            if(getPackageName(e).startsWith("com.alibaba.druid")){
                printLog(j,e);
                sendMail(j);
            }else {
                Handler handler = getNextHandler();
                if(handler != null){
                    getNextHandler().holder(j,e);
                }
            }
        }
    }

    /**
     * 网络异常处理
     * */
    private static class NetExceptionHandler extends Handler{

        @Override
        <T extends Exception> void holder(JSONObject j, T e) {

            if(getPackageName(e).startsWith("java.net")){
                printLog(j,e);
                sendMail(j);
            }else {
                Handler handler = getNextHandler();
                if(handler != null){
                    getNextHandler().holder(j,e);
                }
            }
        }
    }

    /**
     * 事务异常处理
     * */
    private static class TransactionExceptionHandler extends Handler{

        @Override
        <T extends Exception> void holder(JSONObject j, T e) {

            if(getPackageName(e).startsWith("org.springframework.transaction") ||
               getPackageName(e).startsWith("org.apache.ibatis.transaction")){
                printLog(j,e);
                sendMail(j);
            }else {
                Handler handler = getNextHandler();
                if(handler != null){
                    getNextHandler().holder(j,e);
                }
            }
        }
    }

    /**
     * SQL异常处理
     * */
    private static class SQLExceptionHandler extends Handler{

        @Override
        <T extends Exception> void holder(JSONObject j, T e) {

            if(getPackageName(e).startsWith("java.sql") ||
               getPackageName(e).startsWith("com.mysql.jdbc.exceptions") ||
               getPackageName(e).startsWith("org.springframework.jdbc") ||
               getPackageName(e).startsWith("org.apache.ibatis.datasource")){
                printLog(j,e);
                sendMail(j);
            }else {
                Handler handler = getNextHandler();
                if(handler != null){
                    getNextHandler().holder(j,e);
                }
            }
        }
    }

}
