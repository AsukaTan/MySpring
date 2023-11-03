[TOC]

# Goal

This project aims to concrete my understanding in Spring by building a simple spring.

# Note

## 原始IoC：如何通过BeanFactory实现原始版本的IoC容器？

### IoC 容器

IoC 容器，也就是 BeanFactory，存在的意义就是将创建对象与使用对象的业务代码解耦。

实际上我们只需要几个简单的部件：我们用一个部件来对应 Bean 内存的映像，一个定义在外面的 Bean 在内存中总是需要有一个映像的。一个 XML reader 负责从外部 XML 文件获取 Bean 的配置，也就是说这些 Bean 是怎么声明的，我们可以**写在一个外部文件里，然后我们用 XML reader 从外部文件中读取进来**。我们还需要一个反射部件，负责加载 Bean Class 并且创建这个实例；创建实例之后，我们用一个 Map 来保存 Bean 的实例；最后我们提供一个 getBean() 方法供外部使用。我们这个 IoC 容器就做好了。最后我们提供一个 getBean() 方法供外部使用。

> xml外存 → bean在内存中的映像

### 实现一个原始版本的 IoC 容器

#### 导入 dom4j-1.6.1.jar 包

原始版本 Bean，我们先只管理两个属性

id 与 class

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
    <bean id = "xxxid" class = "com.minis.xxxclass"></bean>
</beans>
```

#### 构建 BeanDefinition

简单的bean组成

```java
private String id;
private String className;
```

#### 实现 ClassPathXmlApplicationContext

核心通过反射机制创造bean实例

```java
URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
```

### 解耦 ClassPathXmlApplicationContext

1. 创造bean factory接口以及实现类(实现bean的注册和获取)
2. 创造resource接口以及实现类(高扩展性，不仅仅可以读取xml，后续扩展DB和Web资源配置)，同时继承`Iterator<Object>`，提供使用迭代器供其他类调用
3. 创造XmlBeanDefinitionReader类，使用resource迭代器将xml中的element分别注册给bean factory
4. 编写测试类
