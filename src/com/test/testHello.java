package com.test;

import com.mini.ClassPathXmlApplicationContext;

public class testHello {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        Aservice aservice = (Aservice) ctx.getBean("aservice");
        aservice.sayHello();
    }
}
