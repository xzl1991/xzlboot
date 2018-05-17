package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @auther xzl on 18:30 2018/5/9
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 18000, redisNamespace = "ermas-web")
public class SessionConfig {

}
