package com.mini.beans;

import com.mini.exception.BeansException;

public interface BeanFactory {
    //获取bean
    Object getBean(String beanName) throws BeansException;
    //注册bean
    void registerBeanDefinition(BeanDefinition beanDefinition);

}
