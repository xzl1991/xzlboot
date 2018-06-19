package com.springLoad.circle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther xzl on 13:01 2018/6/5
 */
@Getter
@Setter
@Component
public class ClassA {
    @Autowired
    private ClassB classB;
}
