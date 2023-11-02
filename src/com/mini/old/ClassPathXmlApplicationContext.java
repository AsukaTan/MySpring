package com.mini.old;

import com.mini.beans.BeanDefinition;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目前的 ClassPathXmlApplicationContext 兼具了 BeanFactory 的功能，
 * 它通过 singletons 和 beanDefinitions 初步实现了 Bean 的管理，
 * 其实这也是 Spring 本身的做法。后面我会进一步扩展的时候，会分离这两部分功能，
 * 来剥离出一个独立的 BeanFactory。
 */
@Deprecated
public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();
    //构造器获取外部配置，解析出Bean的定义，形成内存映像
    public ClassPathXmlApplicationContext(String fileName) {
        //获取xml信息
        this.readXml(fileName);
        //根据信息实例化bean
        this.instanceBeans();
    }

    /**
     *
     * @param fileName
     * 配置在 XML 内的 Bean 信息都是文本信息，需要解析之后变成内存结构才能注入到容器中。
     */
    private void readXml(String fileName) {
        SAXReader saxReader = new SAXReader();
        try {
            URL xmlPath =
                    this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            //获取xml的根元素
            Element rootElement = document.getRootElement();
            List<Element> elementList = (List<Element>)rootElement.elements();
            //对配置文件中的每一个<bean>，进行处理
            for (Element element : elementList) {
                //获取Bean的基本信息
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID,
                        beanClassName);
                //将Bean的定义存放到beanDefinitions
                beanDefinitions.add(beanDefinition);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用反射创建Bean实例，并存储在singletons中
     */
    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                //通过反射将BeanDefinition 的 class 的类全名变为创建实例对象
                singletons.put(beanDefinition.getId(),
                        Class.forName(beanDefinition.getClassName()).newInstance());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //这是对外的一个方法，让外部程序从容器中获取Bean实例，会逐步演化成核心方法
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}