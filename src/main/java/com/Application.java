package com;

import com.utils.PIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @auther xzl on 18:39 2018/5/9
 * @SpringBootApplication
 @Slf4j
 @EnableScheduling
 */
//@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        //获取运行时应用本身进程ID
//        int pid = PIDUtil.getPID();
        log.info("项目启动 current pid is : {}", System.currentTimeMillis());
//        log.info("项目启动 current pid is : {}", PIDUtil.getPID());
    }
}
