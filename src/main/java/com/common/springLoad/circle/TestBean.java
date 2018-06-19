package com.common.springLoad.circle;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @auther xzl on 16:37 2018/6/7
 */
@Getter
@Setter
public class TestBean {
    public String name;
    static HashMap hashMap = new LinkedHashMap();
    static {
        hashMap.put("","");
    }
}
