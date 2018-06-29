package com;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther xzl on 13:36 2018/6/28
 *
 * 测试 pidstat
 *
 */
public class HoldeCpuTest {
    public static class HoldCpuTask implements Runnable{
        @Override
        public void run() {
            while (true){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                double d = Math.random() * Math.random();
            }
        }
    }
    public static class LzayTask implements Runnable{
        @Override
        public void run() {
            try {
                while (true){
                    Thread.sleep(2000);
                    System.out.println("空闲线程："+Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("线程即将启动-----等待10s：");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new HoldCpuTask());//占用cpu
        executorService.submit(new HoldCpuTask());//空闲线程
        executorService.submit(new HoldCpuTask());//空闲线程
        executorService.submit(new HoldCpuTask());//空闲线程
    }
}
