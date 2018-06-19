package com.common.springLoad.circle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @auther xzl on 17:01 2018/6/5
 */
@Getter
@Setter
public class MyFactoryBean implements FactoryBean,InitializingBean {
    private String name;

    private TestBean test;

    private DependentBean depentBean;

    @Override
    public Object getObject() throws Exception {
        if(null == test){
            afterPropertiesSet();
        }
        return test;
    }

    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }

    @Override
    public boolean isSingleton() {
//        return false;
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("***********************");
        System.out.println(" name :----- :" + this.name );
        if(null == test){
            test = new TestBean();
            test.name = depentBean.doSomething() + this.name;
        }
        System.out.println("***********************");
    }
}
