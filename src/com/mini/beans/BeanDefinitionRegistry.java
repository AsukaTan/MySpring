package com.mini.beans;

/**
 * 仓库: 可以存放、移除、获取及判断 BeanDefinition 对象
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition bd);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}