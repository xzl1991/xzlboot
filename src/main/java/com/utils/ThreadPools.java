package com.utils;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xlizy on 2017/4/19.
 *
 * 各种线程池
 * 参考:http://ifeve.com/java-threadpool/
 *     http://www.cnblogs.com/exe19/p/5359885.html
 */
public class ThreadPools {

    //发送报警邮件的线程池
    public static ThreadPoolExecutor mailPool = new ThreadPoolExecutor(10, 20, 1, TimeUnit.MINUTES,new SynchronousQueue<Runnable>());

    //zookeeper监听线程池
    public static ThreadPoolExecutor zkListenerPool = new ThreadPoolExecutor(5, 10, 1, TimeUnit.MINUTES,new SynchronousQueue<Runnable>());


}
