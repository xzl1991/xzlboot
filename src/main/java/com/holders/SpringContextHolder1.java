//package com.holders;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.TypeConverter;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.DependencyDescriptor;
//import org.springframework.beans.factory.config.NamedBeanHolder;
//import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
///**
// * Created by xlizy on 2017/3/29.
// */
//@Component
//public class SpringContextHolder1 extends AbstractAutowireCapableBeanFactory implements ApplicationContextAware {
//
//    private static ApplicationContext applicationContext; // Spring应用上下文环境
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        SpringContextHolder1.applicationContext = applicationContext;
//    }
//
//    public static ApplicationContext getApplicationContext() {
//        return applicationContext;
//    }
//
//    public static <T> T getBeans(String name) throws BeansException {
//        return (T) applicationContext.getBean(name);
//    }
//
//    public static <T> T getBeans(Class<T> cla) throws BeansException {
//        return (T) applicationContext.getBean(cla);
//    }
//
//
//    @Override
//    public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
//        return null;
//    }
//
//    @Override
//    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
//        return null;
//    }
//
//    @Override
//    protected boolean containsBeanDefinition(String beanName) {
//        return false;
//    }
//
//    @Override
//    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
//        return null;
//    }
//
//    @Override
//    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
//        return null;
//    }
//
//    @Override
//    public <T> T getBean(Class<T> requiredType) throws BeansException {
//        return null;
//    }
//
//}
