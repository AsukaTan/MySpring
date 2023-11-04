package com.mini.beans;

import com.mini.exception.BeansException;

/**
 * 新增 Singleton、Prototype 的判断，获取 Bean 的类型
 */
public interface BeanFactory {
    Object getBean(String name) throws BeansException;
    boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);
}
