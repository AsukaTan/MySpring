package com.mini.beans;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObj);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
    String[] getSingletonNames();
}
