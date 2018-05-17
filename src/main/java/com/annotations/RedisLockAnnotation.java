package com.annotations;

import java.lang.annotation.*;

/**
 * @auther xzl on 13:05 2018/5/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface RedisLockAnnotation {
    /** 键 */
    String key() ;//SmsProtService-init
}
