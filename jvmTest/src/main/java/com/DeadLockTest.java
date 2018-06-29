package com;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther xzl on 15:00 2018/6/29
 * 模拟死锁
 */
public class DeadLockTest implements Runnable{
    //静态共享
   static Object object1 = new Object(),object2 = new Object();
    int step;
    public  DeadLockTest(int step){
        this.step = step;
    }
    public static void main(String[] args) {
//        new Thread(new DeadLockTest(0),"任务1").start();
//        new Thread(new DeadLockTest(1),"任务2").start();
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.submit(new DeadLockTest(0));
        service.submit(new DeadLockTest(1));
    }

    @Override
    public void run() {
        if (step ==0 ){
            synchronized (object1){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("当前线程："+Thread.currentThread().getName());
                System.out.println();
                synchronized (object2){
                    System.out.println("第二个锁=======");
                }
            }
        }else {
            synchronized (object2){
                System.out.println("当前线程："+Thread.currentThread().getName());
                System.out.println();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object1){
                    System.out.println("第1个锁=======");
                }
            }
        }
    }
}
