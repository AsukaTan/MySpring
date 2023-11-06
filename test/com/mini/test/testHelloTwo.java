package com.mini.test;

import com.mini.context.ClassPathXmlApplicationContext;
import com.mini.exception.BeansException;
import org.dom4j.DocumentException;


public class testHelloTwo {
    public static void main(String[] args) throws DocumentException {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService;
        try {
            aService = (AService)ctx.getBean("aservice");
            aService.sayHello();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

}