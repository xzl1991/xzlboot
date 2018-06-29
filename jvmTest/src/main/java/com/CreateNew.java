package com;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther xzl on 16:26 2018/6/29
 *  jmap -histo:live  26145
 */
public class CreateNew {
    public static void main(String[] args) {
        System.out.println("开始创建对象");
        List list = new ArrayList();
        for (int i=0;i<1000000;i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            int[] ints = new int[512];
            list.add(ints);
        }
    }
}
