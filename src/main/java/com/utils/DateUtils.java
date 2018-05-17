package com.utils;

import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @auther xzl on 10:18 2018/5/10
 */
@Service
public class DateUtils {
    private Calendar calendar ;
    public Calendar getCanldate(){
        if (calendar==null){
            calendar = Calendar.getInstance();
        }
        return  calendar;
    }
}
