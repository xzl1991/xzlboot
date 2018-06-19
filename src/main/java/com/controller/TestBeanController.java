package com.controller;

import com.common.springLoad.circle.ClassA;
import com.common.springLoad.circle.ClassB;
import com.holders.SpringContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther xzl on 11:06 2018/6/6
 */
@Controller
@RequestMapping("/beans")
public class TestBeanController {
    @RequestMapping("/test")
    @ResponseBody
    public String beans(){
        System.out.println("循环依赖-----");
        ClassA classA = SpringContextHolder.getBean(ClassA.class);
        ClassB classB = SpringContextHolder.getBean(ClassB.class);
        return classA.toString()+":"+classB.toString();
    }
}
