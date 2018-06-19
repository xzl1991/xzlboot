///*
// * Copyright 2002-2017 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.Intercepter;
//
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
//import org.springframework.beans.factory.support.StaticListableBeanFactory;
//
///**
// * Default implementation of the
// * {@link org.springframework.beans.factory.ListableBeanFactory} and
// * {@link BeanDefinitionRegistry} interfaces: a full-fledged bean factory
// * based on bean definition objects.
// *
// * <p>Typical usage is registering all bean definitions first (possibly read
// * from a bean definition file), before accessing beans. Bean definition lookup
// * is therefore an inexpensive operation in a local bean definition table,
// * operating on pre-built bean definition metadata objects.
// *
// * <p>Can be used as a standalone bean factory, or as a superclass for custom
// * bean factories. Note that readers for specific bean definition formats are
// * typically implemented separately rather than as bean factory subclasses:
// * see for example {@link PropertiesBeanDefinitionReader} and
// * {@link org.springframework.beans.factory.xml.XmlBeanDefinitionReader}.
// *
// * <p>For an alternative implementation of the
// * {@link org.springframework.beans.factory.ListableBeanFactory} interface,
// * have a look at {@link StaticListableBeanFactory}, which manages existing
// * bean instances rather than creating new ones based on bean definitions.
// *
// * @author Rod Johnson
// * @author Juergen Hoeller
// * @author Sam Brannen
// * @author Costin Leau
// * @author Chris Beams
// * @author Phillip Webb
// * @author Stephane Nicoll
// * @since 16 April 2001
// * @see StaticListableBeanFactory
// * @see PropertiesBeanDefinitionReader
// * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
// */
//@SuppressWarnings("serial")
//public class MyDefaultListableBeanFactory extends DefaultListableBeanFactory  {
//
//	static {
//		System.out.println("***********~~~~***********************");
//	}
//
//}
