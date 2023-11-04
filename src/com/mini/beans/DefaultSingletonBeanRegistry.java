package com.mini.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    //容器中存放所有bean的名称的列表
    protected List<String> beanNames = new ArrayList<>();
    //容器中存放所有bean实例的map
    protected Map<String, Object> singletonObjects =new ConcurrentHashMap<>(256);
    protected Map<String,Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    /**
     *  锁住当前的ConcurrentHashMap,确保在并发情况还可以对bean进行管理
     * @param beanName
     * @param singletonObject
     */
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }
    }
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    /**
     * 删除时确保线程安全
     * @param beanName
     */
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.beanNames.remove(beanName);
            this.singletonObjects.remove(beanName);
        }
    }
    protected boolean hasDependentBean(String beanName) {
        return false;
    }
    protected String[] getDependentBeans(String beanName) {
        return null;
    }
    protected String[] getDependenciesForBean(String beanName) {
        return null;
    }
}