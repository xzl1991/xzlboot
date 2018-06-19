package com.common.springLoad.circle;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * @auther xzl on 16:40 2018/6/7
 */
public class DependentBean {
    public String doSomething(){
        return "DependentBean :";
    }

    @Autowired
    private TestBean testBean;
}
