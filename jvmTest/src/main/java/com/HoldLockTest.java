package com;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther xzl on 11:22 2018/6/28
 *
 * 线程频繁切换  --- 测试 vmstat
 */
public class HoldLockTest {
    public static Object[] objects = new Object[10];
    public static Random random = new Random();
    static {
        for (int i=0;i<objects.length;i++){
            objects[i] = new Object();
        }
    }
    public static class Task implements Runnable{
        private int i ;
        public Task(int i){
            this.i = i;
        }
        @Override
        public void run() {
            try {
                while (true){
                    synchronized (objects[i]){
                        if (i%2==0){
                            objects[i].wait(random.nextInt(10));

                        }else
                            objects[i].notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(objects.length*2);
        for (int i=0;i<objects.length<<2;i++){
            executorService.submit(new Task(i/2));
        }

    }
}
