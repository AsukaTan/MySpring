package com.mini.test;

import com.mini.beans.BeanFactory;
import com.mini.beans.SimpleBeanFactory;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;
import com.mini.core.XmlBeanDefinitionReader;
import com.mini.exception.BeansException;
import org.dom4j.DocumentException;

public class testHelloTwo {
    public static void main(String[] args) throws DocumentException, BeansException {
        Resource resource = new ClassPathXmlResource("beans.xml");
        BeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader ctx = new XmlBeanDefinitionReader(beanFactory);
        ctx.loadBeanDefinitions(resource);
        Aservice aservice = (Aservice)beanFactory.getBean("aservice");
        aservice.sayHello();
    }
}
