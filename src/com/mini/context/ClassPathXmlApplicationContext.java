package com.mini.context;

import com.mini.beans.BeanDefinition;
import com.mini.beans.BeanFactory;
import com.mini.beans.SimpleBeanFactory;
import com.mini.core.ClassPathXmlResource;
import com.mini.core.Resource;
import com.mini.core.XmlBeanDefinitionReader;
import com.mini.event.ApplicationEvent;
import com.mini.event.ApplicationEventPublisher;
import com.mini.exception.BeansException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (旧)
 * 目前的 ClassPathXmlApplicationContext 兼具了 BeanFactory 的功能，
 * 它通过 singletons 和 beanDefinitions 初步实现了 Bean 的管理，
 * 其实这也是 Spring 本身的做法。后面我会进一步扩展的时候，会分离这两部分功能，
 * 来剥离出一个独立的 BeanFactory。
 * （新）
 * ApplicationContext:
 * 容器不仅是容器，还兼具发布事件的功能。
 * 由于simplebeanfactory注册时候会创建非懒bean, 所以现在applicationContext启动的时候就会创建所有非懒加载bean。
 * 之前的版本并不会创建bean,要到获取bean的时候才创建bean，现在对getBean方法的调用提前到注册beanDefinition的时候了
 */
public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher{
    SimpleBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) throws DocumentException {
        Resource res = new ClassPathXmlResource(fileName);
        SimpleBeanFactory bf = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(res);
        this.beanFactory = bf;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
    }

    @Override
    public boolean isSingleton(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}