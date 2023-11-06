package com.mini.core;

import com.mini.beans.BeanDefinition;
import com.mini.beans.BeanFactory;
import com.mini.beans.SimpleBeanFactory;
import com.mini.property.ArgumentValue;
import com.mini.property.ArgumentValues;
import com.mini.property.PropertyValue;
import com.mini.property.PropertyValues;
import org.dom4j.Element;

import java.util.ArrayList;
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
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                //如果有value，则没有ref。如果没有value，ref值带入pV，同时有ref
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            String[] refArray = refs.toArray(new String[0]);//dump the list with String only
            beanDefinition.setDependsOn(refArray);

            //end of handle properties

            //读构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ArgumentValue(pType,pName,pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            //注册bean
            this.bf.registerBeanDefinition(beanID,beanDefinition);
        }

    }



}