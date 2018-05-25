package com.utils;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @auther xzl on 10:18 2018/5/10
 */
@Service
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar calendar ;
    public Calendar getCanldate(){
        if (calendar==null){
            calendar = Calendar.getInstance();
        }
        return  calendar;
    }

    /**
     * @author xzl
     * @Description 获取当月最后一天
     * @Param
     * @Date  2018-05-23 13:28:39
     */
    public static String getLastDayOfMonth(String dateString) {
        Calendar cal = null;
        try {
            cal = DateUtils.toCalendar(sdf2.parse(dateString));
            cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  sdf2.format(cal.getTime());
    }
}
