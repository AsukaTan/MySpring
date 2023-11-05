package com.mini.core;

import com.mini.beans.BeanDefinition;
import com.mini.beans.BeanFactory;
import com.mini.beans.SimpleBeanFactory;
import com.mini.property.ArgumentValue;
import com.mini.property.ArgumentValues;
import com.mini.property.PropertyValue;
import com.mini.property.PropertyValues;
import org.dom4j.Element;

import java.util.List;


/**
 * 本类将bean注册进入bean factory
 */
public class XmlBeanDefinitionReader {
    SimpleBeanFactory bf;
    public XmlBeanDefinitionReader(SimpleBeanFactory bf) {
        this.bf = bf;
    }

    /**
     * 将每个resource注册给bean factory
     * 自动迭代，不需要自己写方法调用
     * @param res
     */
    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element)res.next();
            String beanID=element.attributeValue("id");
            String beanClassName=element.attributeValue("class");

            BeanDefinition beanDefinition=new BeanDefinition(beanID,beanClassName);

            //读属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                PVS.addPropertyValue(new PropertyValue(pType, pName, pValue));
            }
            beanDefinition.setPropertyValues(PVS);
            //end of handle properties

            //读构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ArgumentValue(pValue,pType,pName));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            this.bf.registerBeanDefinition(beanID,beanDefinition);
        }

    }



}