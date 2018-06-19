package com;


import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther xzl on 13:03 2018/6/5
 */
public class Test {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("开始任务------------------");
        AtomicInteger integer = new AtomicInteger(10);
        System.out.println("开始初始化容器:"+integer);
        integer.addAndGet(1);
        System.out.println("++ :"+integer);
        boolean status = false;
        change(status,integer);
        stopWatch.stop();
//        stopWatch.start("结束任务------------------");
        System.out.println("结束任务------------------ ："+stopWatch.prettyPrint());
        System.out.println("处理完："+status+" | "+integer);
    }
    public static void change(boolean status,AtomicInteger integer){
        integer.set(0);
        status = true;
    }
}
