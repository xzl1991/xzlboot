package com.Number;

import java.util.Calendar;

/**
 * @auther xzl on 13:07 2018/5/29
 */
public class TestNum {
    public static void main(String[] args) {
        long star = 1000;
        Calendar calendar = Calendar.getInstance();
        System.out.println("日期： "+ calendar.get(Calendar.HOUR_OF_DAY ) );
//        int i = 0;
//        i = i++ + ++i;
//        System.out.println(i);
//        RateLimiter limiter =  RateLimiter.create(1, 1000, TimeUnit.MILLISECONDS);
//        long start = System.currentTimeMillis();
//        for (int i =0;i<400;i++){
//            if (i%50 ==0){
//                limiter.acquire(3);
////                System.out.println(i+"&&&&&&&&&&&&&"+limiter.acquire(3));
//                System.out.println("&&&&&&&&&&&&&"+(System.currentTimeMillis()-start));
//            }else {
//                limiter.acquire();
////                System.out.println(i+"******"+limiter.acquire());
//                System.out.println("******"+(System.currentTimeMillis()-start));
//            }
//        }
    }
}
