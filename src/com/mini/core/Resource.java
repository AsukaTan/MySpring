package com.mini.core;

/**
 * 外部的配置信息都当成 Resource（资源）来进行抽象
 * 有了Resource接口后可以扩展
 * eg.从DB, Web拿信息
 */
public interface Resource extends Iterable<Object>{
    public boolean hasNext();
    public Object next();
}
