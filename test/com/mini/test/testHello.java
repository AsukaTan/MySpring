package com.mini.test;

import com.mini.old.ClassPathXmlApplicationContext;

/**
 * 测试的为old
 */
public class testHello {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
       Aservice aservice = (Aservice) ctx.getBean("aservice");
        aservice.sayHello();
    }
}
