package com;

import com.common.Movie;
import com.common.springLoad.circle.ClassA;
import com.common.springLoad.circle.ClassB;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @auther xzl on 13:03 2018/6/5
 */
public class BeanLifeCycle {
    public static void main(String[] args) {
        System.out.println("开始初始化容器");
        //region 这里模拟循环依赖 加载问题 ++++ refresh 设置成false 切 setAllowCircularReferences false 报异常
        //普通bean的循环依赖
        ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext(new String[]{"my-application.xml"},false);
        factory.setAllowCircularReferences(false);
        System.out.println("******************************************");
        Movie movie = (Movie) factory.getBean("movie");
        ClassA classA = (ClassA) factory.getBean("classA");
        ClassB classB = (ClassB) factory.getBean("classB");
        //endregion
        System.out.println(classA);
        System.out.println(classB);

        System.out.println("现在开始关闭容器！******************************************");
        factory.registerShutdownHook();
    }
}
