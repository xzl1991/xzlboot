package com.BlockQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther xzl on 15:30 2018/5/11
 */
public class TestMove {
    public static void main(String[] args) {
        System.out.println("200个人在抢票~~");
        Movie movie = new Movie();
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0;i<200;i++){
            pool.execute(new Watch(movie,"姓名:"+i+"--@@@@@@"));
        }
    }
}
