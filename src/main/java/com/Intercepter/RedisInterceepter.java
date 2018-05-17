package com.Intercepter;

import com.Service.RedisLock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.annotations.RedisLockAnnotation;
import com.utils.LogUtils;
import com.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.management.relation.Relation;
import java.lang.annotation.*;
import java.util.Set;

/**
 * @auther xzl on 13:00 2018/5/14
 */
@Aspect
@Component
@Slf4j
public class RedisInterceepter {
    @Autowired
    private  RedisTemplate redisTemplate;
//    @Autowired
//    protected RedisLock redisLock;
    @Around("@annotation(com.annotations.RedisLockAnnotation) && @annotation(getLock)")
    public Object refreshCache(ProceedingJoinPoint pjp, RedisLockAnnotation getLock){
        log.debug("{}获取锁", LogUtils.getPrefix());
        StringBuilder builder = new StringBuilder(100);
        //原来的
        Object value = null;
        if(getLock != null){
            final String lockKey = getLock.key();

            final String ipListKey = "smsIpListRedisKeyQAAA";
            log.debug("{},key:{}",LogUtils.getPrefix(),JSON.toJSONString(getLock.key()));
            //访问目标方法的参数：
            Object[] args = pjp.getArgs();
            int status = 0;
            if (args != null && args.length > 0 ) {
//                args[0] = "改变后的参数1";
                status = (int) args[0];
            }

            if(StringUtils.isNotEmpty(lockKey)){
                log.debug("{}自定义key不为空,删除key为{}的缓存",LogUtils.getPrefix(),lockKey);
                try (RedisLock redisLock = new RedisLock(redisTemplate,lockKey);){
                    if (status<0){
                        throw new RuntimeException("自定义异常");
                    }
                    if(redisLock.lock()){
                        //不为空
                        JSONArray jsonArray = new JSONArray();
                        JSONObject object ;
                        if (redisTemplate.opsForList().size(ipListKey)==0){
                            for (int j=0;j<2;j++){
                                int i=0;
                                for (;i<5;i++){//存放数据ip和端口
                                    jsonArray = new JSONArray();
                                    jsonArray.add("httpHeadip+smsSendUrl:"+j);
                                    jsonArray.add(i);
                                    redisTemplate.opsForList().leftPush(ipListKey,jsonArray);
                                }
                            }

//                            redisTemplate.opsForList().leftPushAll(ipListKey,jsonArray);
                        }
//                        value = redisTemplate.opsForList().leftPop(ipListKey).toString();
//                        value = JSONArray.parse(redisTemplate.opsForList().leftPop(ipListKey).toString());
                        value = redisTemplate.opsForList().leftPop(ipListKey);
                        redisTemplate.opsForList().rightPush(ipListKey,value);//放回
                        JSONArray arr = new JSONArray();arr.add("httpHeadip+smsSendUrl:1");
                        arr.add(2);
                        redisTemplate.opsForList().remove(ipListKey,0,arr.toJSONString());
                        redisTemplate.opsForList().range(ipListKey,0,-1);
//                        redisTemplate.opsForList().remove(ipListKey,0,value);//移除
//                        builder.append(redisTemplate.opsForList().range(ipListKey,0,-1)).append("::拿到的值：").append(value)
//                                .append("<新的：").append(redisTemplate.opsForList().range(ipListKey,0,-1));
                    }
//                    value =  builder.toString();

                }catch (Exception e){
                    try {
                        value =  pjp.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
