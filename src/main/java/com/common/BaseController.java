package com.common;

/**
 * @auther xzl on 19:23 2018/5/9
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.LogUtils;
import com.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Component @Slf4j
public abstract class BaseController {


    public <T> String objectToRet(T t){

        JSONObject jo = new JSONObject();
        jo.put("success", true);
        jo.put("data", JSON.toJSONString(t));
        return jo.toJSONString();

    }

    /**
     * 解析查询条件
     * @param obj 参数对象
     * */
    protected <T> Map<String,Object> getConditions(T obj){
        return RequestUtils.getConditions(obj);
    }

    /**
     * 解析查询条件
     * @param obj 参数对象
     * @param request 从中获取分页等信息
     * */
    protected <T> Map<String,Object> getConditions(T obj,HttpServletRequest request){
        return RequestUtils.getConditions(obj,request);
    }


    /**
     * 统一错误处理
     *
     * ajax请求返回json信息
     * 普通同步请求跳转至错误页
     * */
    @ExceptionHandler @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, final Exception ex){
        ModelAndView mv = new ModelAndView("error/error");
        if(ex != null){
            //针对开发、运维人员的处理，为他们发送消息
//            ExceptionHolder.holder(ex);
            try {
                //如果是ajax请求响应头会有，x-requested-with
                String xrw = request.getHeader("x-requested-with");
                if ("XMLHttpRequest".equals(xrw) || "Ext.basex".equals(xrw)) {
                    JSONObject error = new JSONObject();
                    error.put("result","systemError");
                    error.put("msg","系统处理错误,请联系管理员");
                    error.put("details",ex.getMessage());
                    response.setContentType("application/text;charset=UTF-8");
                    response.getWriter().write(error.toJSONString());
                    return null;
                }else{
                    mv.addObject("ERROR_MSG",ex.getMessage());
                    mv.addObject("ERROR_DETAILS",ex.getMessage());
                }
            } catch (IOException e1) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e1.printStackTrace(pw);
                log.error("{}Controller统一错误处理器报错:"+sw.toString(), LogUtils.getPrefix());
            }
        }
        return mv;

    }


}
