package com.mini.beans;

import com.mini.exception.BeansException;
import com.mini.property.ArgumentValue;
import com.mini.property.ArgumentValues;
import com.mini.property.PropertyValue;
import com.mini.property.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 既是一个工厂同时也是一个仓库
 * 同时也是单例仓库的子类
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry{
    //key: beanName, value: BeanDefinition
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    //List of beanName
    private List<String> beanDefinitionNames = new ArrayList<>();
    private Map<String, Object> earlySingletonObjects = new HashMap<>(256);


    /**
     * 对getBean方法的调用提前到注册beanDefinition的时候了
     * @param name
     * @param beanDefinition
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        //不是慢加载，则立即创建bean加入容器
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
            }
        }
    }
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    /**
     * 直接调用BeanDefinitionRegistry的signleton
     * 每个类只实现自己类所设定的功能，否则类功能混乱
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getBean(String beanName) throws BeansException{
        //直接从容器中获得单例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            //如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if(singleton == null){
                System.out.println("get bean null -------------- " + beanName);
                BeanDefinition bd = beanDefinitionMap.get(beanName);
                singleton=createBean(bd);
                this.registerBean(beanName, singleton);
                // 预留beanpostprocessor位置
                // step 1: postProcessBeforeInitialization
                // step 2: afterPropertiesSet
                // step 3: init-method
                // step 4: postProcessAfterInitialization
            }
        }
        if (singleton == null) {
            throw new BeansException("bean is null.");
        }
        return singleton;
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);

    }
    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = doCreateBean(bd);

        this.earlySingletonObjects.put(bd.getId(), obj);

        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        handleProperties(bd, clz, obj);

        return obj;
    }

    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clz = Class.forName(bd.getClassName());
        //handle constructor
        ArgumentValues argumentValues = bd.getConstructorArgumentValues();
        if (!argumentValues.isEmpty()) {
            //创造空数组
            Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
            Object[] paramValues = new Object[argumentValues.getArgumentCount()];
            //将参数导入数组
            for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                    paramTypes[i] = String.class;
                    paramValues[i] = argumentValue.getValue();
                } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                    paramTypes[i] = Integer.class;
                    paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                } else if ("int".equals(argumentValue.getType())) {
                    paramTypes[i] = int.class;
                    paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                } else {
                    paramTypes[i] = String.class;
                    paramValues[i] = argumentValue.getValue();
                }
            }
            try {
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException |
                     SecurityException e) {
                e.printStackTrace();
            }
        } else {
            obj = clz.newInstance();
        }

    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        e.printStackTrace();
    }

    System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());

    return obj;
    }
    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        //handle properties
        System.out.println("handle properties for bean : " + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();

                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                //不是ref则普通加载
                if (!isRef) {
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                } else {
                    /**
                     * 对 ref 所指向的另一个 Bean 再次调用 getBean() 方法，
                     * 这个方法会获取到另一个 Bean 实例，这样就实现了另一个 Bean 的注入。
                     * 这样一来，如果有多级引用，就会形成一个多级的 getBean() 调用链。
                     * 由于在调用 getBean() 的时候会判断容器中是否包含了 bean instance，
                     * 没有的话会立即创建，所以 XML 配置文件中声明 Bean 的先后次序是任意的。
                     */
                    try {
                        //找到拥有该class作为域的class
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String) pValue);
                    } catch (BeansException e) {
                        throw new RuntimeException(e);
                    }
                }

                //手动构造set方法
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

                Method method = null;
                try {
                    //通过set方法名和参数拿到该方法
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    //调用set方法set property
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void refresh(){
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

//
//
///**
// * 确保我们通过 SimpleBeanFactory 创建的 Bean 默认就是单例的
// * 增加了对 containsBean 和 registerBean的实现，皆为对单例bean的操作
// */
//public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory{
//    //存入从String name 映射到该BeanDefinition的hashmap
//    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
//    private List<String> beanDefinitionNames = new ArrayList<>();
//    public SimpleBeanFactory() {}
//
//    //getBean，容器的核心方法
//
//    /**
//     * getBean方法中如果该bean存在于beanDefinitions，但是没有实例化的时候，会实例化该bean
//     * @param beanName
//     * @return
//     * @throws BeansException
//     */
//    public Object getBean(String beanName) throws BeansException {
//        //先尝试直接拿bean实例
//        Object singleton = this.getSingleton(beanName);
//        //如果此时还没有这个bean的实例，则获取它的定义来创建实例
//        if (singleton == null) {
//            //获取bean的定义
//            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
//            //xml配置错误，没有该bean
//            if (beanDefinition == null) {
//                throw new BeansException("No bean.");
//            }
//            try {
//                //反射
//                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            //新注册这个bean实例
//            this.registerSingleton(beanName, singleton);
//        }
//        return singleton;
//    }
//
//    public void registerBeanDefinition(BeanDefinition beanDefinition) {
//        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
//        this.bea
//    }
//    public boolean containsBean(String name) {
//        return containsSingleton(name);
//    }
//    public void registerBean(String beanName, Object obj) {
//        this.registerSingleton(beanName, obj);
//    }
//}
