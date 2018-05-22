package com.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.utils.StringUtils;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by xlizy on 2017/3/30.
 *
 * web项目的过滤器定义(对应传统项目的web.xml里的Filter)
 */
@Configuration
public class FilterConfig {

    //扫描路径
    private static final Collection urlPatterns1 = new ArrayList<String>(){{add("/*");}};
    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    @Bean
    public FilterRegistrationBean CharacterEncodingFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.setOrder(1);
        registrationBean.setUrlPatterns(urlPatterns1);
        return registrationBean;
    }

    //嵌入SSO ------- start
//    @Bean
//    public FilterRegistrationBean P3PFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
////        registrationBean.setFilter(new P3PFilter());
//        registrationBean.setOrder(10);
//        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean CASSingleSignOutFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new SingleSignOutFilter());
//        registrationBean.setOrder(11);
//        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean CASFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new AuthenticationFilter());
//        registrationBean.setOrder(12);
////        registrationBean.setInitParameters(new HashMap<String, String>(){{
////            put("casServerLoginUrl",casServerLoginUrl);
////            put("serverName",callBackUrl);
////            put("excludes","/services/*");
////        }});
////        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean CASValidationFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
//        registrationBean.setOrder(13);
////        registrationBean.setInitParameters(new HashMap<String, String>(){{
////            put("casServerUrlPrefix",casServerUrlPrefix);
////            put("serverName",callBackUrl);
////        }});
//        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean CASHttpServletRequestWrapperFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
//        registrationBean.setOrder(14);
//        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean CASAssertionThreadLocalFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new AssertionThreadLocalFilter());
//        registrationBean.setOrder(15);
//        registrationBean.setUrlPatterns(urlPatterns1);
//        return registrationBean;
//    }
    //嵌入SSO ------- end

    @Bean
    public FilterRegistrationBean CommonParameters(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new javax.servlet.Filter() {
            public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {}

            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                if(servletResponse instanceof HttpServletResponse){
                    HttpServletResponse response = (HttpServletResponse)servletResponse;
                    //全局解决跨域方法2  这两个设置只存在第一个时候  方法一无效
//                    response.setHeader("Access-Control-Allow-Origin","*");
                    //允许跨域
//                    response.setHeader("Access-Control-Allow-Headers","*");
                }
                if(servletRequest instanceof HttpServletRequest){
                    HttpServletRequest request = (HttpServletRequest) servletRequest;
                    servletRequest.setAttribute("CP_CTXPATH",request.getContextPath());
//                    servletRequest.setAttribute("CP_FULLPATH",serverHost + request.getContextPath());
                    servletRequest.setAttribute("CP_CURRENTDATETIME",System.currentTimeMillis()/(3600*1000*2));
                    //如果不是开发环境，则静态资源后面追加.min后缀
                    servletRequest.setAttribute("CP_SRS",((!"local".equals(springProfilesActive))?".min":""));

                    //添加登录用户信息
//                    String loginName = AssertionHolder.getAssertion().getPrincipal().getName();
//                    Object ps = request.getSession().getAttribute("CP_PERMISSION");


//                    if(ps == null && StringUtils.isNotEmpty(loginName)){
////                        try {
////                            HttpClientUtil httpClientUtil = SpringContextHolder.getBean(HttpClientUtil.class);
////                            String rep = httpClientUtil.sendHttpGet(SpringContextHolder.getBean(InterfaceAdd.class).getSSO()+"services/ssoClientService/getPermissionsByLoginName/"+loginName+".json");
////                            JSONArray array = JSON.parseArray(rep);
////                            //SSOClient ssoClient = SpringContextHolder.getBean(SSOClient.class);
////                            //Collection<String> permissions = ssoClient.getSsoClientService().getPermissionsByLoginName(loginName);
////                            if(array!=null){
////                                request.getSession().setAttribute("CP_PERMISSION",array.toArray());
////                            }
////                        } catch (BeansException e) {
////                            ExceptionHolder.holder(e);
////                        }
//                    }
                    servletRequest.setAttribute("CP_PERMISSION",request.getSession().getAttribute("CP_PERMISSION"));
//                    servletRequest.setAttribute("CP_USERNAME", loginName);
                    if("cdn".equals(SystemConfig.getGetResourcesWay())){
                        servletRequest.setAttribute("CP_SR_PATH", "//xxx/extjs-6.0.1/");
                    }else{
//                        servletRequest.setAttribute("CP_SR_PATH", serverHost + request.getContextPath() + "/ext6/");
                    }
                    servletRequest.setAttribute("LOCAL_CACHE_VERSION_FOR_Dictionary", SystemConfig.getLocalCacheVersionFor_Dictionary());
                    servletRequest.setAttribute("LOCAL_CACHE_VERSION_FOR_CollectOrgAll",SystemConfig.getLocalCacheVersionFor_CollectOrgAll() );
                    servletRequest.setAttribute("LOCAL_CACHE_VERSION_FOR_CollertorAll",SystemConfig.getLocalCacheVersionFor_CollertorAll() );
//                    String oldName = "";
//                    try {
//                        oldName = AssertionHolder.getAssertion().getPrincipal().getName();
//                    } catch (Exception e) {}
//                    if(StringUtils.isNotEmpty(loginName)){
////                        OrganizationContextHolder.setUsername(loginName);
////                        OrganizationContextHolder.setCurrentOrg(CommonConstants.ORG);
////                        OrganizationContextHolder.setUsername(loginName);
//                    }


                }
                filterChain.doFilter(servletRequest,servletResponse);
            }

            public void destroy() {}
        });
        registrationBean.setOrder(16);
        registrationBean.setUrlPatterns(urlPatterns1);
        return registrationBean;
    }
}
