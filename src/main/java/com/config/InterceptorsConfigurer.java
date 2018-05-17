package com.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.LogUtils;
import com.utils.StringUtils;
import com.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.utils.RequestUtils.getIpAddr;


/**
 * Created by xlizy on 2017/4/21.
 *
 * 拦截器
 */
@Configuration
//@Slf4j
public class InterceptorsConfigurer extends WebMvcConfigurerAdapter {

    //拦截路径
    private static final String urlPatterns1 = "/**";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            //在请求处理之前进行调用（Controller方法调用之前）
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if(handler instanceof HandlerMethod){
                    String ipAddr = getIpAddr(request);
                    if(StringUtils.isEmpty(ThreadLocals.requestId.get())){
                        ThreadLocals.requestId.set(UUID.randomUUID().toString());
                    }
                    if(StringUtils.isEmpty(ThreadLocals.requestIP.get())){
                        ThreadLocals.requestIP.set(ipAddr);
                    }
                    JSONObject requestInfo = new JSONObject();
                    requestInfo.put("Accept",request.getHeader("Accept"));
                    requestInfo.put("Accept-Encoding",request.getHeader("Accept-Encoding"));
                    requestInfo.put("Accept-Language",request.getHeader("Accept-Language"));
                    requestInfo.put("Connection",request.getHeader("Connection"));
                    requestInfo.put("Cache-Control",request.getHeader("Cache-Control"));
                    requestInfo.put("Content-Length",request.getHeader("Content-Length"));
                    requestInfo.put("Content-Type",request.getHeader("Content-Type"));
                    requestInfo.put("Cookie",request.getHeader("Cookie"));
                    requestInfo.put("Host",request.getHeader("Host"));
                    requestInfo.put("Origin",request.getHeader("Origin"));
                    requestInfo.put("Referer",request.getHeader("Referer"));
                    requestInfo.put("User-Agent",request.getHeader("User-Agent"));
                    requestInfo.put("X-Requested-With",request.getHeader("X-Requested-With"));
                    requestInfo.put("Request URL",request.getRequestURL());
                    requestInfo.put("Request URI",request.getRequestURI());
                    requestInfo.put("Request Method",request.getMethod());
                    requestInfo.put("Request RealIP",ipAddr);

                    Map<String,String[]> param = request.getParameterMap();
                    if(param != null){
                        Set<String> ks = param.keySet();
                        if(ks.size() > 0){
                            JSONArray jps = new JSONArray();
                            for (String k : ks) {
                                JSONObject p = new JSONObject();
                                p.put(k,param.get(k));
                                jps.add(p);
                            }
                            requestInfo.put("Form Data",jps);
                        }

                    }

//                    log.info("{}requestInfo:{}", LogUtils.getPrefix(),requestInfo.toJSONString());
                }
                return true;
            }

            //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
            public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
                ThreadLocals.requestId.remove();
                ThreadLocals.requestIP.remove();
//                DBContextHolder.clearDBType();
            }

            //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
            public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

            }
        }).addPathPatterns(urlPatterns1);
        super.addInterceptors(registry);
    }
}
