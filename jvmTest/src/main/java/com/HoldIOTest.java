package com;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther xzl on 14:03 2018/6/28
 */
public class HoldIOTest {
    public static class HoldIOTask implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    File  file = new File("temp");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    for (int i=0;i<100000;i++){
                        outputStream.write(i);//大量写
                    }
                    outputStream.close();
                    FileInputStream inputStream = new FileInputStream(file);
                    int content;
                    while ((content=inputStream.read())!=-1){
                        System.out.println("读取的数据： "+content);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new HoldIOTask());//占用cpu
        executorService.submit(new HoldeCpuTest.HoldCpuTask());//空闲线程
        executorService.submit(new HoldeCpuTest.HoldCpuTask());//空闲线程
        executorService.submit(new HoldeCpuTest.HoldCpuTask());//空闲线程
    }
}
