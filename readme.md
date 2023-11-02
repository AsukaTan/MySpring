# Goal

This project aims to concrete my understanding in Spring by building a simple spring.

# Note

## 原始IoC：如何通过BeanFactory实现原始版本的IoC容器？

### IoC 容器

IoC 容器，也就是 BeanFactory，存在的意义就是将创建对象与使用对象的业务代码解耦。

实际上我们只需要几个简单的部件：我们用一个部件来对应 Bean 内存的映像，一个定义在外面的 Bean 在内存中总是需要有一个映像的。一个 XML reader 负责从外部 XML 文件获取 Bean 的配置，也就是说这些 Bean 是怎么声明的，我们可以**写在一个外部文件里，然后我们用 XML reader 从外部文件中读取进来**。我们还需要一个反射部件，负责加载 Bean Class 并且创建这个实例；创建实例之后，我们用一个 Map 来保存 Bean 的实例；最后我们提供一个 getBean() 方法供外部使用。我们这个 IoC 容器就做好了。最后我们提供一个 getBean() 方法供外部使用。

> xml外存 → bean在内存中的映像

![img](https://static001.geekbang.org/resource/image/a3/c3/a382d7774c7aa504231721c7d28028c3.png?wh=1905x1253)

### 实现一个原始版本的 IoC 容器

原始版本 Bean，我们先只管理两个属性：id 与 class。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
    <bean id = "xxxid" class = "com.minis.xxxclass"></bean>
</beans>
```

