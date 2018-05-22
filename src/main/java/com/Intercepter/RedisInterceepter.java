package com.Intercepter;

import com.Service.RedisLock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.annotations.RedisLockAnno;
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
    /* @author xzl
     * @Description 获取锁
     * @Param
     * @Date  2018-05-22 10:58:20
     */
//    @Around("@annotation(com.annotations.RedisLockAnno) && @annotation(redisLock )")
//    public Object redisLock(ProceedingJoinPoint pjp, RedisLockAnno redisLock){
//        //取出的值
//        if(redisLock != null){
//            //锁住的key
//            final String lockKey = redisLock.key();
//            //存放队列的key
//            log.info("{},key:{}",LogUtils.getPrefix(),JSON.toJSONString(redisLock.key()));
//            if(StringUtils.isNoneEmpty(lockKey)){
//                log.debug("{}自定义key不为空,获取锁{}",LogUtils.getPrefix(),lockKey);
//                try (RedisLock lock = new RedisLock(redisTemplate,lockKey);){
//                    if (lock.lock()){
//                        pjp.proceed();
//                    };
//                }catch (Exception e){
//                    log.debug("{},获取锁失败,key:{}",LogUtils.getPrefix(),JSON.toJSONString(redisLock.key()));
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
    @Around("@annotation(com.annotations.RedisLockAnnotation) && @annotation(getLock)")
    public Object refreshCache(ProceedingJoinPoint pjp, RedisLockAnnotation getLock){
        log.debug("{}获取锁", LogUtils.getPrefix());
        StringBuilder builder = new StringBuilder(100);
        //原来的
        Object value = null;
        if(getLock != null){
            final String lockKey = getLock.key();
            log.debug("{},key:{}",LogUtils.getPrefix(),JSON.toJSONString(getLock.key()));
            //访问目标方法的参数：
            Object[] args = pjp.getArgs();
            int status = 0;
            if (args != null && args.length > 0 ) {
//                args[0] = "改变后的参数1";
//                status = (int) args[1];
            }

            if(StringUtils.isNotEmpty(lockKey)){
                log.debug("{}自定义key不为空,删除key为{}的缓存",LogUtils.getPrefix(),lockKey);
                try (RedisLock redisLock = new RedisLock(redisTemplate,lockKey);){
                    if (status<0){
                        throw new RuntimeException("自定义异常");
                    }
                    if(redisLock.lock()){
                      return  pjp.proceed();
                    }
                }catch (Exception e){
                    try {
                        value =  pjp.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return value;
    }

}
