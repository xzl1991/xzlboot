package com.controller;

import com.alibaba.fastjson.JSONArray;
import com.annotations.RedisLockAnnotation;
import com.common.BaseController;
import com.common.Movie;
import com.model.SmsSendParam;
import com.utils.DateUtils;
import com.utils.LogUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther xzl on 18:47 2018/5/9
 */
@Controller
@RequestMapping(value = "/xzl/test")
@Slf4j
@Api(value = "TestController", description = "测试controller")
public class TestController extends BaseController {
    @Value("${interface.url}")
    String value;
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    protected Movie movie;
    //    @Autowired
//    protected RedisLock redisLock;
    private  String startKey= System.nanoTime()+"";
    //    DateUtils dateUtils;
    Calendar calendar = Calendar.getInstance();
    long time = System.currentTimeMillis();
    @RequestMapping("/redis")
    @ResponseBody
    public String redis(){
        redisTemplate.opsForValue().set(startKey,startKey);
        log.debug("{}将短信修复开始时间放入缓存中,默认值:{},redisKey:{}", LogUtils.getPrefix(),startKey,startKey);
        redisTemplate.expire(startKey, 60, TimeUnit.SECONDS);
        return redisTemplate.opsForValue().get(startKey).toString();
    }

    @RequestMapping("/val")
    @ResponseBody
    public String redisGetByKey(){
//        redisTemplate.opsForValue().set(startKey,startKey);
        log.debug("{}将短信修复开始时间放入缓存中,默认值:{},redisKey:{}", LogUtils.getPrefix(),startKey,startKey);
//        redisTemplate.expire(startKey, 60, TimeUnit.SECONDS);
        return redisTemplate.opsForValue().get(startKey).toString();
    }

    @RequestMapping("/del")
    @ResponseBody
    public String delRedis(){
        redisTemplate.delete(startKey);
        log.debug("{}将短信修复开始时间放入缓存中,默认值:{},redisKey:{}", LogUtils.getPrefix(),startKey,startKey);
        return redisTemplate.opsForValue().get(startKey)+":-------------新的值";
    }


    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        String num = movie.tackClass();
        System.out.println(value);
        System.out.println("获取眼镜-------------"+movie);
        if (num.indexOf("4")>-1){
            System.out.println("这个不好送回去~~");
            movie.returnClass(num);
        }
        return "index:"+num;
    }
    @RequestMapping("/index1")
    @ApiImplicitParam(name = "smsSendParam", value = "实体smsSendParam", required = true, dataType = "SmsSendParam")
    public String index1( @RequestBody SmsSendParam smsSendParam){
        log.error("{}Controller====测试报错:", LogUtils.getPrefix());
        System.out.println("获取到的 calendar1 ===="+calendar+":当前时间:"+time);
        System.out.println("方法内获取到的 calendar1 ===="+Calendar.getInstance());
        return "index";
    }

    @ApiOperation(value="状态查询", notes="状态查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态:数据生成方式,>0 从redis获取,其他本地", required = true ,dataType = "int"),

    })
    @RequestMapping(value = "/init/{status}")
    @ResponseBody
//    @RedisLockAnnotation(key = "SmsProtService-init")
    public JSONArray testLock(HttpServletRequest request, @PathVariable int status){//testServiceDubbo

        final String ipListKey = "smsIpListRedisKey";
        //原来的
        StringBuilder builder = new StringBuilder(100);
        String value ;
//        try(RedisLock redisLock = new RedisLock(redisTemplate,"SmsProtService-init")) {
//        try {
//            if(redisLock.lock()){
//                //不为空
//                if (redisTemplate.opsForList().size(ipListKey)==0){
//                    redisTemplate.opsForList().leftPushAll(ipListKey,new String[]{"A","B","C","D"});
//                }
//                value = redisTemplate.opsForList().leftPop(ipListKey).toString();
//                redisTemplate.opsForList().rightPush(ipListKey,value);//放回
//                builder.append(redisTemplate.opsForList().range(ipListKey,0,-1)).append("::拿到的值：").append(value)
//                        .append("<新的：").append(redisTemplate.opsForList().range(ipListKey,0,-1));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        JSONArray array =  ((TestController)AopContext.currentProxy()).getIpPort(status);
        System.out.println(array.toJSONString());
        return array;
    }

    //    @RedisLockAnnotation(key = "SmsProtService-init")
//    @ResponseBody
    @RedisLockAnnotation(key = "SmsProtService-init")
    public JSONArray getIpPort(int status){
        JSONArray jsonArray = new JSONArray();
        String[] ips = new String[]{"abc","ABC","CNN"};
        jsonArray.add(ips[new Random().nextInt(ips.length)]);
        jsonArray.add(new Random().nextInt(32));
        return  jsonArray;
    }
}