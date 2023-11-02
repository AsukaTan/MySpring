package com.mini.core;

import com.mini.beans.BeanDefinition;
import com.mini.beans.BeanFactory;
import org.dom4j.Element;


/**
 * 本类将bean注册进入bean factory
 */
public class XmlBeanDefinitionReader {
    BeanFactory beanFactory;
    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 将每个resource注册给bean factory
     * 自动迭代，不需要自己写方法调用
     * @param resource
     */
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()){
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            this.beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
