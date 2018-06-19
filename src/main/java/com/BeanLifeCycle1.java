package com;

import com.common.Movie;
import com.common.springLoad.circle.ClassA;
import com.common.springLoad.circle.ClassB;
import com.common.springLoad.circle.MyFactoryBean;
import com.common.springLoad.circle.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @auther xzl on 13:03 2018/6/5
 *
 * 原文 ： http://ifeve.com/%E8%AE%BAspring%E4%B8%AD%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96%E7%9A%84%E6%AD%A3%E7%A1%AE%E6%80%A7%E4%B8%8Ebean%E6%B3%A8%E5%85%A5%E7%9A%84%E9%A1%BA%E5%BA%8F%E5%85%B3%E7%B3%BB/
 */
public class BeanLifeCycle1 {
    public static void main(String[] args) {
        System.out.println("开始初始化容器");
        //region 这里模拟循环依赖 加载问题 ++++ refresh 设置成false 切 setAllowCircularReferences false 报异常
        //普通bean的循环依赖
        ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext(new String[]{"my-application.xml"});
//        factory.setAllowCircularReferences(false);
        System.out.println("******************************************");
//        MyFactoryBean myFactoryBean = (MyFactoryBean) factory.getBean("myFactoryBean");
        TestBean  testBean = (TestBean) factory.getBean("myFactoryBean");
        System.out.println(testBean.name);
        System.out.println("现在开始关闭容器！******************************************");
        factory.registerShutdownHook();
    }
}
