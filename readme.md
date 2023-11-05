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

Q: synchronized + ConcurrentHashMap 同时使用的意义？

> A: 它只是这个容器层面的，扩大到业务逻辑，可能由多个操作组成，业务操作上对同一个key值的业务数据在同时读写，这个业务逻辑层面的并发控制仍然还是要由我们自己来控制的。
>
> 除了ConcurrentHashMap存储数据之外，再用一个ConcurrentHashMap单独存储锁，如<key,  Object>,(注意：Java中的锁时所在对象上的，所以我们随便用一个Object就可以当锁)。进行业务操作的时候，从locl  map中先拿锁，然后用synchronized(lock){}将业务操作包起来保证并发安全和原子性。
>
> JDK 7
>
> Segment锁
>
> ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c814a3b049894b8d9abccf1480c3a113~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)
>
> JDK 8
>
> **CAS + synchronized**
>
> ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/cc7d08086ca0484c999edf9d72e9dfd2~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)



## Update 11/5/2023

1. #### 可以从xml文件读取property value和constructor带入目标类

   1. 新增`propertyValue`, `ArgumentValue`类。以及`propertyValues`, `ArgumentValues`类成为容器类。

   2. 修改`BeanDefinition`, 加入新参数如`singleton`或者`prototype`,以及`propertyValues`, `ArgumentValues`，承载新功能

   3. 增加`BeanDefinition`的仓库方法`BeanDefinitionRegistry`，去更删改查`BeanDefinition`

   4. 对于单例模式下的bean，增加`SingletonBeanRegistry`接口，并且创造`DefaultSingletonBeanRegistry`类

      Q: 为什么`SimpleBeanFactory`不同时声明实现`SingletonBeanRegistry`并且继承它的默认实现类呢?

      > A:  SimpleBeanFactory对外只希望外界知道自己是一个beanFactory和beanDefinitionRegistry，至于singletonBeanRegistry，它只希望作为一种内部的能力来使用，所以继承一个已经实现的类来拥有能力，但是声明接口的时候不声明这个接口。

   
