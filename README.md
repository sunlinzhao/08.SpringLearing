> 学习前置条件：
>
> + JavaEE
> + 数据库：MySQL、Redis
> + Web 前后端开发：html、jsp、servlet
> + 项目构建管理工具：Maven 
> + 持久层框架：Mybatis、Hibernate
>

<h2 id="qt6hD">简介</h2>
 Spring，2004年3月发布的版本1.0，轻量级的 JavaEE 的解决方案：

> 特点：
>
> 1. 轻量级，体积小，通常在几兆之间；
> 2. 运行环境没有要求；
> 3. 代码可移植性高；
> 4. 非侵入式设计；
> 5. 开源；
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726829635752-5a653c35-975a-46c9-89b5-26c29437129e.png)

<h2 id="TqYpd">设计模式（GOF）</h2>
<h3 id="Jb3w3">工厂模式</h3>
<h4 id="WLRY9">简单工厂模式</h4>
> 1. 属于创建型模式之一
> 2. 动态实例化有共同接口的类：用更抽象的接口去接收具有相同方法的类实例，便于**避免硬编码**方式更换需要实例化的类
> 3. 使用场景
>     1. 当系统中有多个产品类，且它们之间存在共同的接口时。
>     2. 不希望在客户端代码中直接使用 new 关键字创建对象。
>     3. 需要将对象创建的细节对外隐藏，只暴露一个获取对象的接口。
> 4. 缺点：系统中产品种类较多时，工厂类会变得庞大且难以维护，此时可以考虑使用工厂方法模式或抽象工厂模式来替代
>

+ 水果示例

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726831459518-27941775-fa93-45ac-856c-8848cd3e3969.png)

+ Dao 层、Service层示例

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726832261665-0f2778c4-6f93-4b63-a51f-8b9d6f34aa08.png)

通过配置文件方式，**反射机制**实现软编码更换创建对象实例：`[BeanFactory](#ueefb641a)`

```java
public class BeanFactory {
    public static <T> T getBean(String name){
        ResourceBundle bundle = ResourceBundle.getBundle("MyBean");
        String beanName = bundle.getString(name);
        try {
            Class<?> aClass = Class.forName(beanName);
            return (T) aClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
```

```java
userDao=com.slz.springfw.factory.user.UserDaoMybatisImpl
```

```java
public class UserServiceImpl implements UserService {

    @Override
    public void save() {
        //        UserDao UserDao = new UserDaoJdbcImpl();
        //        UserDao UserDao = new UserDaoMybatisImpl();
        UserDao userDao = BeanFactory.getBean("userDao");
        userDao.save();
    }
}
```

<h3 id="CpSWX">单例模式</h3>
> 一个类，只创建一个实例对象，并且自行实例化这个对象，向整个系统提供这个对象的操作;
>
> 原理：
>
> 1. 私有化构造方法，不允许外部通过 new 方法创建实例对象；
> 2. 私有化静态属性，在类的内部创建类自身的实例对象；
> 3. 通过公有静态方法`getInstance`获取实例对象；
>

1. 饿汉式

```java
// 饿汉式
public class SingleTon {
    // 1. 私有化构造方法
    private SingleTon() {
    }
    // 2. 私有化静态属性
    private static SingleTon singleTon = new SingleTon();
    // 3. 公有静态方法 getInstance
    public static SingleTon getInstance() {
        return singleTon;
    }
}
```

2. 懒汉式

```java
// 懒汉式
public class SingleTon {
    // 1. 私有化构造方法
    private SingleTon() {
    }
    // 2. 私有化静态属性
    private static SingleTon singleTon = null;
    // 3. 公有静态方法 getInstance
    public static SingleTon getInstance() {
        if(singleTon==null){
            singleTon = new SingleTon();
        }
        return singleTon;
    }
}
```

> 扩展：双重检测锁
>

```java
public class SingleTon {
    private SingleTon() {
    }
    private static volatile SingleTon singleTon = null; // 使用volatile关键字保证可见性和禁止指令重排
    public static SingleTon getInstance() {
        if (singleTon == null) {
            synchronized (SingleTon.class) {
                if (singleTon == null) {
                    singleTon = new SingleTon();
                }
            }
        }
        return singleTon;
    }
}
```

<h3 id="DwIMs">代理模式</h3>
+ 静态代理 [https://www.yuque.com/u41526838/kb/yr6alg9h5m6s2s51#KnZH9](#KnZH9)
+ 动态代理 [https://www.yuque.com/u41526838/kb/yr6alg9h5m6s2s51#wyNGS](#wyNGS)

<h2 id="uu2uD">Spring 入门</h2>
> 原理：
>
> 1. 读取 Bean 配置文件，获得 Bean 的类名、属性名、属性值；
> 2. 在加载 Spring 容器时，通过**<u>反射机制</u>**，从类名获取构造方法、字段 Field 对象（Spring的实现是获得Method对象，调用setXxx方法）；
> 3. 通过构造方法**<u>创建实例</u>**，通过 Field 对象进行**<u>属性注入</u>**；
>

<h3 id="qKcjq">准备</h3>
1. pom.xml 引入依赖 `spring-context`

```java
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.3.15</version>
</dependency>
```

> spring-context 扩展了Spring核心容器的功能，提供了高级配置和事件处理机制
>

2. 导入依赖后就可以找到右键目录->新建->XML配置->Spring配置，新建 Spring 配置文件：`applicationContext.xml`；

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726835223662-49553285-3817-477d-a478-8ab228353595.png)

3. 将 `applicationContext.xml`配置文件添加到上下文环境；

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726835618834-ac8fdf34-3517-48df-a46e-2159de4dd5cb.png)

<h3 id="T1zO0">配置Bean，并加载 Spring 容器</h3>
> Spring 实现了`BeanFactory`的功能，通常使用 `ApplicationContext`，`ApplicationContext`继承自 `BeanFactory`。
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726836369581-cb0a4806-92e6-497d-bd17-eb37d589a218.png)

1. 在 `applicationContext.xml`种配置 Bean；

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="userDao" class="com.slz.springfw.demo.UserDaoJdbcImpl"></bean>
    <bean id="userDao1" class="com.slz.springfw.demo.UserDaoMybatisImpl"></bean>
</beans>
```

2. 通过`ClassPathXmlApplicationContext`读取 Bean 配置文件，加载 Spring 容器；

```java
public class Test {
    public static void main(String[] args) {
        // 加载 Spring 容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 调用Bean
        // UserDao userDao = (UserDao) ctx.getBean("userDao1");
    }
}
```

> + Spring 容器在加载时，就把 Bean 配置文件中的 bean 全部实例化；
> + 之后可以通过 `getBean` 方法调用对应 Bean 实例对象；
> + 如下图，只运行加载容器代码，配置的 Bean 的构造方法都被调用；
>
> ![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726896607787-18c83b50-1bd0-4956-9f99-4d3bdd2b63a7.png)
>

<h3 id="TfIkJ">属性注入</h3>
1. 配置 Bean

```java
<bean id="Person" class="com.slz.springfw.demo.Person">
  <property name="name" value="张三"></property>
  <property name="gender" value="男"></property>
  <property name="age" value="25"></property>
</bean>
```

2. 加载 Sping 容器
3. 获取 Bean 实例

```java
public class Test {
    public static void main(String[] args) {
        // 加载 Spring 容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 获取实例
        Person person = (Person) ctx.getBean("Person");
        // 打印实例
        System.out.println(person);
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726898070561-4c3d41af-6989-4223-b0a7-62f99e4f4799.png)

<h3 id="y4xtY">依赖注入</h3>
> Person 依赖 Car
>

1. 配置Bean

```java
<bean id="Car" class="com.slz.springfw.demo.Car">
  <property name="band" value="BYD"></property>
</bean>
<bean id="Person" class="com.slz.springfw.demo.Person">
  <property name="name" value="张三"></property>
  <property name="gender" value="男"></property>
  <property name="age" value="25"></property>
  <property name="car" ref="Car"></property>
</bean>
```

2. 加载 Spring 容器
3. 获取 Bean 实例 （同上）

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726900913552-ba9b37fc-f938-4231-ab06-5d3cb4760b17.png)

<h2 id="kApAD">IOC 与 DI</h2>
<h3 id="NMX5i">简介</h3>
> + **IOC**：Inverse Of Control，控制反转，是一种设计模式，它将对象创建和管理的责任从具体的类中分离出来，交给一个容器（通常是框架提供的）；
>     - **降低耦合度**：对象之间不再直接依赖于彼此的具体实现。
>     - **提高可测试性**：更容易模拟和替换依赖对象。
>     - **增强灵活性**：可以通过配置而不是硬编码来改变对象的行为。
> + **DI**：Dependency Injection，依赖注入，是一种设计模式，用于实现控制反转，允许对象在其创建过程中接收其依赖项，而不是在内部创建这些依赖项；
>     - **解耦**：对象不再负责创建自己的依赖项，而是通过外部注入。
>     - **灵活性**：依赖项可以在运行时动态注入，而不是在编译时硬编码。
>     - **可测试性**：依赖项可以通过构造函数、setter方法或其他方式注入，使得单元测试更加容易。
>

<h3 id="Dniho">Bean 的单例模式</h3>
> 两次加载的都是同一个实例对象，Spring默认是Bean单例；[https://www.yuque.com/u41526838/kb/yr6alg9h5m6s2s51#CpSWX](#CpSWX)
>

```java
public class Test {
    public static void main(String[] args) {
        // 加载 Spring 容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 获取实例
        Person person1 = (Person) ctx.getBean("Person");
        Person person2 = (Person) ctx.getBean("Person");
        // 打印实例
        System.out.println(person1==person2);
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1726901072664-2bf20390-96f0-464a-8eb7-d391171b2396.png)

<h3 id="Sbr0f">Spring Bean 属性值注入 - XML配置</h3>
<h4 id="Hg5le">SetXxx方式属性注入</h4>
> Spring 属性值的注入可以使用反射机制获取setXxx方法；
>

+ 三种getBean获取实例的方法

```java
public class Test {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Teacher teacher1 = ctx.getBean("Teacher", Teacher.class);
        Teacher teacher2 = ctx.getBean(Teacher.class);
        Teacher teacher3 = (Teacher) ctx.getBean("Teacher");
    }
}
```

各个数据类型的Bean配置方式是不一样的；

```java
@Data
public class Teacher {
    private TeacherDao teacherDao;
    private String name;
    private String[] addrs;
    private Set<String> set;
    private Set<Teacher> teachers;
    private List<String> list;
    private Map<String, Double> map;
    private Properties properties;
}
```

<h5 id="KpTNk">字符串/数值</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="name" value="高启强"></property>
</bean>
```

```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="name" ><null></null></property>
</bean>
```

<h5 id="oesWf">数组/列表</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="addrs">
    <list>
      <value>平顶山</value>
      <value>许昌</value>
      <value>洛阳</value>
    </list>
  </property>
  <property name="list">
    <list>
        <value>A</value>
        <value>B</value>
        <value>C</value>
    </list>
  </property>
</bean>
```

<h5 id="SA6aL">集合</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="set">
    <set>
      <value>x</value>
      <value>y</value>
      <value>x</value>
    </set>
  </property>
</bean>
```

```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="teachers">
    <set>
      <ref bean="teacher1"></ref>
      <ref bean="teacher2"></ref>
    </set>
  </property>
</bean>
<bean id="teacher1" class="com.slz.springfw.demo.Teacher">
  <property name="name" value="张山"></property>
</bean>
<bean id="teacher2" class="com.slz.springfw.demo.Teacher">
  <property name="name" value="李市"></property>
</bean>
```

<h5 id="Vc4Nb">Map</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="map">
    <map>
      <entry key="key1" value="123.4"></entry>
      <entry key="key2" value="124.7"></entry>
    </map>
  </property>
</bean>
```

<h5 id="LY5ds">Properties</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="properties">
    <props>
      <prop key="key1">value1</prop>
      <prop key="key2">value2</prop>
      <prop key="key3">value3</prop>
    </props>
  </property>
</bean>
```

<h5 id="VaxEN">引用类型</h5>
```java
<bean id="Teacher" class="com.slz.springfw.demo.Teacher">
  <property name="teacherDao" ref="TeacherDaoImpl"></property>
</bean>
<bean id="TeacherDaoImpl" class="com.slz.springfw.demo.TeacherDaoImpl"></bean>
```

<h5 id="SWaKD">XML 配置 Bean 的简化写法</h5>
> + 在Spring框架中，`xmlns:p="[http://www.springframework.org/schema/p"](http://www.springframework.org/schema/p")` 是一个XML命名空间声明；
> + 用于简化属性注入的定义，这个命名空间允许你在不使用`<property>`标签的情况下为bean设置属性值；
> + 当你在Spring配置文件中看到这个声明时，意味着你可以使用`p:`前缀来更简洁地配置bean的属性；
>

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="Boy" class="com.slz.springfw.demo.Boy" p:name="陈平" p:age="25" p:car-ref="Car"></bean>
</beans>
```

<h5 id="QLNUG">id 和 name 属性</h5>
> id和name的区别：name相当于别名，可以有多个，id只能有一个。
>

```java
<bean id="Boy" name="boy,boy1,boy2" class="com.slz.springfw.demo.Boy" p:name="陈平" p:age="25" p:car-ref="Car"></bean>
```

<h4 id="jkzfr">Spring 构造方法属性注入</h4>
> Spring 属性值的注入也可以使用反射机制获取有参构造方法；
>

```java
public class Student {
    private String name;
    private Integer age;
    private Car car;

    private Student() {
    }

    private Student(String name, Integer age, Car car) {
        this.name = name;
        this.age = age;
        this.car = car;
    }
}
```

<h5 id="Zrbug">四种赋值方式</h5>
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns:p="http://www.springframework.org/schema/p"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <!-- 1. 默认顺序赋值 -->
  <bean id="Student1" class="com.slz.springfw.demo.Student">
    <constructor-arg value="张浩"></constructor-arg>
    <constructor-arg value="25"></constructor-arg>
    <constructor-arg ref="Car"></constructor-arg>
  </bean>
  <!-- 2. 通过类型赋值 -->
  <bean id="Student2" class="com.slz.springfw.demo.Student">
    <constructor-arg type="java.lang.Integer" value="25"></constructor-arg>
    <constructor-arg type="java.lang.String" value="张浩"></constructor-arg>
    <constructor-arg type="com.slz.springfw.demo.Car" ref="Car"></constructor-arg>
  </bean>
  <!-- 3. 通过名字赋值 -->
  <bean id="Student3" class="com.slz.springfw.demo.Student">
    <constructor-arg name="age" value="25"></constructor-arg>
    <constructor-arg name="name" value="张浩"></constructor-arg>
    <constructor-arg name="car" ref="Car"></constructor-arg>
  </bean>
  <!-- 4. 通过索引赋值 -->
  <bean id="Student4" class="com.slz.springfw.demo.Student">
    <constructor-arg index="1" value="25"></constructor-arg>
    <constructor-arg index="0" value="张浩"></constructor-arg>
    <constructor-arg index="2" ref="Car"></constructor-arg>
  </bean>
</beans>
```

<h5 id="qViEj">简化赋值方方式</h5>
> `xmlns:c="[http://www.springframework.org/schema/c"](http://www.springframework.org/schema/c")`，在Spring配置文件中看到这个声明时，意味着你可以使用`c:`前缀来更简洁地配置bean的属性；
>

```java
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns:p="http://www.springframework.org/schema/p"
  xmlns:c="http://www.springframework.org/schema/c"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <!-- 构造参数简化赋值 -->
  <bean id="Student" class="com.slz.springfw.demo.Student" c:name="张浩" c:age="22" c:car-ref="Car"></bean>
</beans>
```

<h4 id="EsAaA">自动注入</h4>
> + **引用类型**可以通过`autowire`完成自动注入（自动装配）；
> + 使用 setXxx 方式属性注入
>

<h5 id="r2kfW">byName</h5>
> 按名字来自动装配，名字指的是类中的属性名和xml配置文件中引用的<bean>的 id 名相同
>

<h5 id="xo2Yn">byType</h5>
> 按照属性的类型自动装配， 要求在容器中（<bean>配置文件中），只能存在一个属该性类型的对象，按类型装配需满足：
>
> + java 类中属性的类型和容器中 bean 的 class 类型完全相同；
> + java 类中属性的类型(父类)和容器中 bean 的 class 类型(子类)是父类和子类关系；
> + java 类中属性的类型(接口)和容器中 bean 的 class 类型(实现类)是按口和实现类关系；
>

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns:p="http://www.springframework.org/schema/p"
  xmlns:c="http://www.springframework.org/schema/c"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <bean id="car" class="com.slz.springfw.demo.Car">
    <property name="band" value="BYD"></property>
  </bean>
  <!-- 引用类型自动注入 - 按名字 -->
  <bean id="StudentX" class="com.slz.springfw.demo.Student" p:name="sss" p:age="23" autowire="byName"></bean>
  <!-- 引用类型自动注入 - 按类型 -->
  <bean id="StudentY" class="com.slz.springfw.demo.Student" p:name="张浩" p:age="22" autowire="byType"></bean>
</beans>
```

<h3 id="svY5y">Spring Bean 属性值注入 - 注解</h3>
> 1. 在**<font style="color:#DF2A3F;">类或属性</font>**上面加入需要使用的注解；
> 2. 在 Spring 的配置文件中，加入一个**<font style="color:#DF2A3F;">组件扫描的元素</font>**(对Spring中的组件自动扫描)；
> 3. 注解可以帮助 Spring 容器**<font style="color:#DF2A3F;">识别并管理</font>**这些 Bean；
>

+ SpringFrameWork 框架提供的注解：
    - `@Component`
    - `@Repository`
    - `@Service`
    - `@Controller`
    - `@Value`
    - `@Autowired`
+ JDK 提供的注解：
    - `@Resource`

<h4 id="ZeZy0">使用在类上的注解 - 声明 Bean</h4>
> + `@Component`
> + `@Repository`
> + `@Service`
> + `@Controller`
>
> 作用：声明该类是一个Bean
>

<h5 id="Auxir">@Component</h5>
代表是 Spring 容器中的组件

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd">

  <!-- 配置组件扫描路径 -->
  <context:component-scan base-package="com.slz.springfw.annotation"></context:component-scan>

</beans>
```

```java
@Data
@Component // 声明组件
public class Teacher {
    private String name;
    private Integer age;
}
```

```java
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        // 使用注解时，这里使用class引入，而不是名字
        Teacher teacher = context.getBean(Teacher.class);
        System.out.println(teacher);
    }
}
```

1. @Component，不提供参数，默认配置的 id 为类名首字母小写，如：Teacher 类可以通过 teacher 引入；
2. @Component("TeacherTest")，注解参数相当于配置的 id，就可以通过 TeacherTest 引入该 bean 实例；

<h5 id="mX43h">@Repository / @Service / @Controller</h5>
1. `@Component`/`@Repository` `@Service` / `@Controller`实现的**功能是一样的**，就是声明该类是一个组件，等待被扫描，这一点从下面注解的定义也可以了解到；
2. 但是它们使用的**场景还是有区别的**：
    1. `@Component`：应用在普通Bean上；
    2. `@Repository`：应用在持久层组件上；TeacherDao
    3. `@Service`：应用在业务逻辑层组件上；TeacherService
    4. `@Controller`：应用在控制层组件上；TeacherController

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
    String value() default "";
}
```

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
}
```

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
}
```

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
}
```

<h4 id="Lm2jL">使用在属性上的注解 - 自动装配</h4>
> + `@Value`
> + `@Autowired`
> + `@Resource`
>

<h5 id="XbWNr">@Value</h5>
用在基本数据类型和字符串上的属性注入：

1. 明文赋值 @Value("xxx")

```java
@Data
@Component // 声明组件
public class 
{
    @Value("赵雷")
    private String name;
    @Value("25")
    private Integer age;
}
```

2. 配置文件赋值 @Value("${xxx}")
    1. 配置文件配置 properties 文件路径，让Spring容器加载属性文件；
    2. 在properties中写入要配置的键值 xxx=yyy；
    3. 在java程序中通过 @Value("${xxx}") 注入属性值；

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd">

  <!-- 配置组件扫描路径 -->
  <context:component-scan base-package="com.slz.springfw.annotation"></context:component-scan>
  <!-- 在容器中加载属性文件 -->
  <context:property-placeholder location="prop.properties"></context:property-placeholder>
</beans>
```

```java
Teacher.name=Bob
Teacher.age=23
```

```java
@Data
@Component // 声明组件
public class Teacher {
    @Value("${Teacher.name}")
    private String name;
    @Value("${Teacher.age}")
    private Integer age;
}
```

<h5 id="mNVtI">@Autowired</h5>
+ 用在引用类型上的属性注入；
+ @Autowired 默认想通过构造方法的方式进行属性注入（会报警告），但通过setXxx方式也是可以的；
+ 支持 byName / byType，默认情况，使用 byType 自动注入的处理；
    - 如果使用byName方式，需要另外加注解 @Qualifier，如果没有在@Component 配置类的别名，默认是类名首字母小写；

```java
@Autowired
@Qualifier("student")
private Student student;
```

+ 有一个参数 required：
    - boolean required=true(默认)，如果对象没有注入，报错；
    - boolean required=false，如果对象没有注入，会获取null值；

```java
@Component // 声明组件
@ToString
public class Teacher {
    @Value("${Teacher.name}")
    private String name;
    @Value("${Teacher.age}")
    private Integer age;
    private Student student;
    @Autowired
    public Teacher(Student student) {
        this.student = student;
    }
}
```

<h5 id="NYepJ">@Resource</h5>
1. @Resource 是JDK提供的一个注解，需要在 pom.xml 中引入依赖；

```java
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.2</version>
</dependency>
```

2. 引用类型注入，支持 byName / byType，默认情况，使用 byName 自动注入处理；

```java
@Resource
private Student student;
```

> @Autowired 与 @Resource的区别：
>
> 1. @Autowired 由Spring提供，@Resource 由JDK提供；
> 2. @Autowired 默认 byType， @Resource 默认 byName；
> 3. @Autowired 注入时，会产生警告并建议使用构造方法注入，@Resource 不会有警告；
>
> **<font style="color:#DF2A3F;">Spring4开始，推荐使用构造方法注入，构造方法注入就只能使用@Autowired，不能使用@Resource；</font>**
>

**总结：如果要使用字段 setXxx 注入，就应用@Resource；如果要用构造方法注入，使用@Autowired。**

<h3 id="LUOJw">非自定义类 Bean 创建</h3>
将非自定义类配置为Bean，交给Spring管理

<h4 id="XLRNS">配置第三方类库的类</h4>
以 HikariCP 数据库连接池为例：

1. pom.xml 中引入依赖

```java
<dependency>
  <groupId>com.zaxxer</groupId>
  <artifactId>HikariCP</artifactId>
  <version>5.0.1</version>
</dependency>
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.28</version>
</dependency>
```

2. prop.properties 配置属性键值

```java
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql://localhost:3306/Mybatis?useSSL=false&serverTimezone=GMT%2B8
jdbc.username=root
jdbc.password=root
```

3. bean.xml 配置 bean，并注入属性值

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd">

  <!-- 第三方类库管理， 数据源-连接池-->
  <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}"></property>
    <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
    <property name="username" value="${jdbc.username}"></property>
    <property name="password" value="${jdbc.password}"></property>
  </bean>
      <!-- 在容器中加载属性文件 -->
    <context:property-placeholder location="prop.properties"></context:property-placeholder>
</beans>
```

4. 测试程序

```java
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        HikariDataSource dataSource = (HikariDataSource) context.getBean("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

<h4 id="MDtsM">配置JDK提供的类</h4>
```java
    <bean id="date" class="java.util.Date"></bean>
```

<h3 id="vMbdg">Spring 多个 Bean 配置文件的使用</h3>
当有很多个 Bean 配置文件时，为了便于管理，可以在配置文件中导入其它配置文件，然后在 Java 程序中获取Bean 实例时，只需要读取一个总的Bean 配置文件就行。

1. applicationContextA.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <!-- 一般写法 -->
  <import resource="classpath:applicationContext_dao.xml"></import>
  <import resource="classpath:applicationContext_service.xml"></import>
  <!-- 简化写方法，通配符前缀匹配, 注意不要通配到文件本身 -->
  <import resource="classpath:applicationContext_*.xml"></import>
</beans>
```

2. applicationContext_dao.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <bean id="dao" class="com.slz.springfw.annotation.dao.TeacherDaoImpl"></bean>
</beans>
```

3. applicationContext_service.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <bean id="service" class="com.slz.springfw.annotation.service.TeacherService" p:dao-ref="dao"></bean>
</beans>
```

4. Java Test.java

```java
public class Test2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContextA.xml");
        TeacherService service = context.getBean("service", TeacherService.class);
        service.teach();
    }
}
```

<h3 id="kHrbx">复杂对象的 Bean 创建、</h3>
> 复杂对象是指，该对象有许多属性，并且不是自定义类，配置成 Bean 比较复杂
>

<h4 id="d1fw0">使用 FactoryBean</h4>
如果要创建一个复杂对象，可以实现 FactoryBean 接口，接口中有三个方法：

1. `isSingleton()`：是 default 默认方法，默认返回 true，单例模式，返回 false 是非单例模式，可以不重写；
2. `getObject()`：获取最后得到的类对象，返回泛型，需要重写；
    1. `getBean("id")`：参数不带前缀 '&'，返回`getObject()`返回的对象；
    2. `getBean("&id")`：参数带前缀 '&'，返回 FactoryBean 的实现类对象；
3. `getObjectType()`：获取对象的 Class 对象字节码对象，需要重写；

```java
public interface FactoryBean<T> {
    String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

    @Nullable
    T getObject() throws Exception;

    @Nullable
    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
```

```java
@Getter
@Setter
public class MyConnectionBean implements FactoryBean<Connection> {
    private String driver;
    private String url;
    private String username;
    private String password;
    @Override
    public Connection getObject() throws Exception {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    /*
     * 通过 FactoryBean 提供创建的对象，是否是单例模式
     * */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
  <bean id="connection" class="com.slz.springfw.factoryBean.MyConnectionBean">
    <property name="driver" value="${jdbc.driverClassName}"></property>
    <property name="url" value="${jdbc.jdbcUrl}"></property>
    <property name="username" value="${jdbc.username}"></property>
    <property name="password" value="${jdbc.password}"></property>
  </bean>
  <!-- 在容器中加载属性文件 -->
  <context:property-placeholder location="MyBean.properties"></context:property-placeholder>
</beans>
```

```java
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("complexBean.xml");
        // 如果给定的id前面没有&符号，代表获取 FactoryBean 中 getObject() 返回的对象
        Connection connection1 = (Connection) context.getBean("connection");
        // 如果给定的 id 前面有 & 符号，代表获取 FactoryBean 实现类的对象
        MyConnectionBean connection2 = (MyConnectionBean) context.getBean("&connection");
        System.out.println(connection1);
        System.out.println(connection2);
    }
}
```

<h4 id="rfRpV">工厂方式创建</h4>
<h5 id="xxTOg">实例工厂</h5>
> 这种方式的好处是，降低了 Spring 框架 API 的侵入
>

1. 创建工厂类

```java
@Setter
@Getter
public class InstanceFactory {
    private String driver;
    private String url;
    private String username;
    private String password;
    public Connection getInstance(){
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```

2. 配置 Bean

```java
<!-- 配置工厂类对象 -->
<bean id="InstanceFactory" class="com.slz.springfw.factoryBean.InstanceFactory">
  <property name="driver" value="${jdbc.driverClassName}"></property>
  <property name="url" value="${jdbc.jdbcUrl}"></property>
  <property name="username" value="${jdbc.username}"></property>
  <property name="password" value="${jdbc.password}"></property>
</bean>
<!-- 调用工厂类的方法创建需要的复杂对象 -->
<bean id="connect" factory-bean="InstanceFactory" factory-method="getInstance"></bean>
    <!-- 在容器中加载属性文件 -->
    <context:property-placeholder location="MyBean.properties"></context:property-placeholder>
```

<h5 id="vTH21">静态工厂</h5>
与实例工厂的区别是，创建实例的方法是静态方法，且不需要配置工厂类的Bean，应为通过类名就可以访方法

1. 创建工厂类

```java
public class StaticFactory {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://locahost:3306/Mybatis?useSSL=false&serverTimezone=GMT%2B8";
    private static final String username = "root";
    private static final String password = "root";
    public static Connection getInstance(){

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

2. 配置 Bean

```java
<!-- 静态工厂 -->
<bean id="staticConnection" class="com.slz.springfw.factoryBean.StaticFactory" factory-method="getInstance"></bean>
```

<h3 id="Jt6g9">类型转换器</h3>
> + 类型转换是在属性值注入时，注入类型和预期类型之间的转换（因为注入的数据类型不满足属性类型要求）；
> + Spring 本身有一些内置类型转换器，对于一些复杂的类型转换，需要自定义类型转换器；
>

1. 转换器开发：实现 `Converter<S, T>` 接口，并实现`T convert(S source)`方法
2. 转换器注册：配置转换器Bean，并注册该转换器 Bean
    1. 转换器注册的 id 必须是 `conversionService`

```java
@Data
public class Baby {
    private String name;
    private String gender;
    private LocalDate birthday;
}
```

```java
@Data
public class DateConverter implements Converter<String, LocalDate> {
    private String pattern;
    @Override
    public LocalDate convert(String source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(source, formatter);
    }
}
```

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:p="http://www.springframework.org/schema/p"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  <!-- 需要转换的对象 -->
  <bean id="baby" class="com.slz.springfw.converter.Baby" p:name="张三" p:gender="男" p:birthday="2024/09/23"></bean>
  <!-- 自定义转换器对象 -->
  <bean id="dateConverter" class="com.slz.springfw.converter.DateConverter" p:pattern="yyyy/MM/dd"></bean>
  <!-- 对类型转换器进行注册,  id="conversionService" 是必须的-->
  <bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
      <set>
        <ref bean="dateConverter"></ref>
      </set>
    </property>
  </bean>
</beans>
```

```java
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("converterBean.xml");
        Baby baby = context.getBean("baby", Baby.class);
        System.out.println(baby);
    }
}
```

<h2 id="WqcX7">AOP</h2>
<h3 id="vgepc">简介</h3>
> + AOP（Aspect Oriented Programming，面向切面编程）是一种编程范式，主要用于解决在面向对象编程（OOP）中出现的**<font style="color:#DF2A3F;">横切关注点问题</font>**。
> + 横切关注点是指那些<font style="background-color:#FBF5CB;">跨越多个对象的行为，比如日志记录、事务管理、安全控制等，这些行为通常在多个地方都需要实现，但在业务逻辑中又不是核心部分</font>。
> + AOP的优点：
>     - **<font style="color:#DF2A3F;">降低耦合度</font>**：通过将横切关注点从业务逻辑中分离出来，使得业务逻辑更加清晰，同时减少了重复代码。
>     - **可维护性**：由于横切关注点被集中管理，因此更容易维护和扩展。
>     - **易于开发**：开发者可以专注于业务逻辑的编写，而不需要关心日志记录、事务控制等非功能性需求。
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727078437402-30a9b730-687b-4f6c-8fb7-0090ad6677c9.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727079067104-81ea9500-025c-4291-890f-25dbe548405b.png)

<h3 id="J5Zn1">代理模式</h3>
> + 代理模式（Proxy Pattern）是一种**<font style="color:#DF2A3F;">结构型设计模式</font>**，它通过提供一个代理来控制对一个对象的访问
> + 这种模式允许客户端通过代理来**<font style="color:#DF2A3F;">间接地访问目标对象</font>**，而不是直接访问。
> + 代理模式的主要目的是在客户端和目标对象之间增加一层间接层，以便可以**<font style="color:#DF2A3F;">添加额外的责任或控制访问</font>**。
> + 代理模式的优点：
>     - 控制对对象的访问；
>     - 隐藏目标对象的具体实现；
>     - 减少系统开销；
>

<h4 id="KnZH9">静态代理</h4>
> 缺点：
>
> + 与被代理对象耦合性强；
> + 代码不符合开闭原则；
>

1. 创建接口 Platform，声明原有功能

```java
public interface Platform {
    void product();
}
```

2. 创建被代理类 Factory，实现该接口

```java
public class Factory implements Platform {
    @Override
    public void product() {
        System.out.println("工厂生产产品");
    }
}
```

3. 创建代理类 FactoryProxy，实现接口 Platform，并持有被代理类 Factory，并添加额外功能

```java
public class FactoryProxy implements Platform{
    private Factory factory;
    @Override
    public void product() {
        add_fun1();
        add_fun2();
        if (factory==null){
            factory=new Factory();
        }
        factory.product();
        add_fun3();
    }
    public void add_fun1(){
        System.out.println("添加功能1");
    }
    public void add_fun2(){
        System.out.println("添加功能2");
    }
    public void add_fun3(){
        System.out.println("添加功能3");
    }
}
```

<h4 id="wyNGS">动态代理</h4>
> JDK 兄终弟及，CGLIB 子承父业
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727089919814-ba3e8cb9-39e6-4fca-bb07-d1c5bd439121.png)

<h5 id="A7oQG">JDK 动态代理</h5>
> + 要求目标对象，即被代理对象必须实现**<font style="color:#DF2A3F;">接口</font>**；
> + 使用 `Proxy`、`Method`、`InvocationHandler` 类库来完成动态代理；
> + **<font style="color:#DF2A3F;">JDK提供</font>**，属于 `java.lang.reflect.*`包下的类；
>

1. 创建功能接口，UserService

```java
public interface UserService {
    void save(String s);
}
```

2. 创建被代理的类，即接口的实现类，目标类对象，UserServiceImpl

```java
public class UserServiceImpl implements UserService{
    @Override
    public void save(String s) {
        System.out.println("保存了" + s);
    }
}
```

3. 创建动态代理类，实现 InvocationHandler 接口

```java
public class MyInvocationHandler implements InvocationHandler {
    // 持有被代理类
    private Object obj;
    public MyInvocationHandler(Object obj){
        this.obj = obj;
    }

    /**
     * 动态代理目标方法的执行
     * 该方法是在代理对象的方法被调用时执行的，用于在目标方法执行前后添加额外的功能
     *
     * @param proxy 代理对象，即生成的代理实例
     * @param method 当前准备执行的方法
     * @param args 方法执行时传递的实际参数
     * @return 目标方法执行的结果
     * @throws Throwable 如果目标方法运行时抛出了异常，则进行抛出
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 目标对象方法执行前添加的功能
        System.out.println("执行前");
        // 获取被代理对象
        Object o = method.invoke(obj, args);
        // 目标对象方法执行后添加的功能
        System.out.println("执行后");
        // 返回被代理对象
        return o;
    }
}
```

4. 使用动态代理

```java
// 获取代理对象实例
public class ProxyUtil {
    public static Object getProxy(Object target, InvocationHandler handler) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                                      target.getClass().getInterfaces(), handler);
    }
}
```

+ 这里 `UserService` 是一个接口，而 `UserServiceImpl2` 是这个接口的一个实现类；
+ 通过这种方式创建的**<font style="color:#DF2A3F;">代理对象 proxy 必须用 </font>**`**<font style="color:#DF2A3F;">UserService</font>**`**<font style="color:#DF2A3F;"> 接口来接收</font>**，因为最终生成的代理对象是实现了 `UserService` 接口的，而不是直接 `UserServiceImpl2` 类型的对象；
+ 这样做保证了调用者只能通过接口定义的方法来操作代理对象，从而确保了动态代理逻辑的正确执行；

```java
public class Test {
    public static void main(String[] args) {
        UserService proxy = (UserService) ProxyUtil.getProxy(new UserServiceImpl(), new MyInvocationHandler(new UserServiceImpl()));
        proxy.save("job");
    }
}
```

5. 还可以对代理的方法进行判断，可以针对某些方法添加额外功能

```java
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 对被代理对象的方法进行判断
    if("save".equals(method.getName())){
        // 目标对象方法执行前添加的功能
        System.out.println("执行前");
        // 获取被代理对象
        Object o = method.invoke(obj, args);
        // 目标对象方法执行后添加的功能
        System.out.println("执行后");
        // 返回被代理对象
        return o;
    } else {
        return method.invoke(obj, args);
    }
}
}
```

<h5 id="iqOTB">CGLIB 动态代理</h5>
> + CGLlB(Code Generation Library)：开源项目，高性能，高质量，**<font style="color:#DF2A3F;">第三方类库</font>**
> + CGLB 动态代理不使用接口，采用**<font style="color:#DF2A3F;">继承</font>**的方式；
> + 目标类是父类，代理类是子类；
> + `org.springframework.cglib.proxy.*`包下的 `Enhancer`、`MethodInterceptor`、`MethodProxy`实现；
>

1. 创建目标类（父类） UserService

```java
public class UserService {
    public void save(String s){
        System.out.println("保存了" + s);
    }
}
```

2. 创建方法拦截器 MyInterceptor

```java
public class MyInterceptor implements MethodInterceptor {

    private Object obj; // 被代理对象
    public MyInterceptor(Object obj){
        this.obj = obj;
    }
    /**
     * 拦截器方法，用于在方法调用前后执行额外的操作
     *
     * @param o            被拦截对象的实例
     * @param method       正在调用的方法对象
     * @param objects      方法调用时传递的参数数组
     * @param methodProxy  方法代理对象，用于调用目标方法
     * @return             返回方法调用的结果，如果抛出异常则为异常信息
     * @throws Throwable   在方法调用或代理过程中可能抛出的异常
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行前");
        Object oo = method.invoke(obj, objects);
        System.out.println("执行后");
        return oo;
    }
}
```

3. 测试

```java
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Enhancer enhancer = new Enhancer();
        // 设置被代理类的类加载器
        enhancer.setClassLoader(userService.getClass().getClassLoader());
        // 设置父类
        enhancer.setSuperclass(userService.getClass());
        // 设置回调
        enhancer.setCallback(new MyInterceptor(userService));
        // 获取代理对象，用父类接收
        UserService o = (UserService) enhancer.create();
        o.save("job");
    }
}
```

<h3 id="hUOQf">AOP 术语</h3>
1. **通知 / 增强 / Advice**：在目标对象上增加的额外的功能，系统监控、事务、权限、日志等应用场景。
2. **连接点 / JoinPoint**：就是目标对象的方法的地方，方法的前或者后都可以连接，包括抛出异常时，都可以连接。
3. **切点 / PointCut**：是连接点的集合，真正将通知切入到业务逻辑的方法；
4. **切面 / Aspect**：切点和通知的结合，在切点的位置，加入了增强。理解如下：
    1. 在什么位置执行？
    2. 执行什么？
    3. 在什么时间点执行？
5. **引入 / Introduction**：允许向现有的类添加新的功能属性。
6. **目标 / Target**：原始的对象，主要专注于自身的业务逻辑。
7. **代理 / Proxy**：通过一个代理对象，完成对目标对象的代理。
    1. 一种是基于JDK的**<font style="color:#DF2A3F;">接口</font>**的代理，实际上是一种兄弟关系；
    2. 一种是基于CGLIB的**<font style="color:#DF2A3F;">类</font>**的代理，实际上是一种父子关系；
    3. <font style="background-color:#FBF5CB;">合成聚合复用原则：能使用组合关系，不使用继承关系</font>；
8. **织入 / Weaving**：将切面无缝整合到目标对象，创建出一个代理对象的过程的描述。

<h3 id="OSiUT">AOP 实现</h3>
1. 实现方式：
    1. Spring 实现的 AOP；
    2. aspectj 实现的 AOP；
2. 通知 / 增强的五大分类：
    1. 前置通知 `<font style="color:#DF2A3F;">@Before</font>`
    2. 后置通知 `<font style="color:#DF2A3F;">@AfterReturning</font>`
    3. 最终通知`<font style="color:#DF2A3F;">@After</font>`
    4. 环绕通知`<font style="color:#DF2A3F;">@Around</font>`
    5. 异常通知`<font style="color:#DF2A3F;">@AfterThrowing</font>`

<h4 id="BVtd6">切点表达式</h4>
> + <font style="color:#8A8F8D;">[可选参数] </font>
> + <font style="color:#DF2A3F;"><必要参数></font>
>

+ **形式**：`**execution(**<font style="color:#8A8F8D;">[访问权限类型]</font> <font style="color:#DF2A3F;"><返回值类型></font> <font style="color:#8A8F8D;">[包名类名]</font>.<font style="color:#DF2A3F;"><方法名(参数列表)> </font><font style="color:#8A8F8D;">[异常列表]</font>**)**`
+ **作用**：切点表达式的作用是在连接点中过滤出切点；
+ **特殊符号含义**：

| **符号** | **定义** |
| :---: | :---: |
| ***** | 0-n 任意字符 |
| **..** | 1. 在方法参数列表，表示任意多个参数<br/>2. 在包名后，表示当前包及其子包路径 |
| **+** | 1. 在类名后，表示当前类及其子类<br/>2. 在接口后表示当前接口和是实现类 |


示例：

```java
// 指定切入点为 com.xzit 包下的所有类的任意公共方法
execution(public * com.xzit.*.*(..))
// 指定切入点为所有前缀是do的方法
execution(* do*(..))
// 指定切入点为 com.xzit 及其子包中的任意类的任意方法
execution(* com.xzit..*.*(..))
// 指定切入点为 Waiter 接口及其实现类的任意方法
execution(* Waiter+.*(..))
// 指定切点所有前缀是 do 的并且有一个 Sting 类型参数的方法
execution(* do*(String))
```

<h4 id="Ko2UX">准备</h4>
1. pom.xml 中引入依赖

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aspects</artifactId>
  <version>5.3.16</version>
</dependency>
```

2. Bean 配置文件，开启 Bean 扫描和 aspectj 自动代理功能

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:aop="http://www.springframework.org/schema/aop"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd 
  http://www.springframework.org/schema/aop 
  https://www.springframework.org/schema/aop/spring-aop.xsd">
  <!--配置Bean扫描-->
  <context:component-scan base-package="com.slz.aop.*"></context:component-scan>
  <!-- 启用 AspectJ 的自动代理功能 -->
  <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
</beans>
```

3. 创建被代理类

```java
// 被代理类作为组件交给Spring容器管理
@Component
public class Waiter {
    // 切点
    public void showMenu(){
        System.out.println("请点菜");
    }
    // 切点
    public void pay(){
        System.out.println("请付款");
    }
}
```

4. 创建通知 / 增强类

```java
// 让当前通知类作为 Spring 容器的组件
@Component
// 使用 AspectJ 实现 AOP
@Aspect
public class MyAdvice {
    // 前置通知
    @Before(value = "execution(* com.slz.aop..*.showMenu(..))")
    public void doBefore(){
        System.out.println("欢迎光临");
        System.out.println("很高兴为您服务");
        System.out.println("-----------");
    }
    // 最终通知
    @After(value = "execution(* com.slz.aop..*.pay(..))")
    public void doAfter(){
        System.out.println("-----------");
        System.out.println("谢谢惠顾，下次再来");
    }
}
```

5. 测试

```java
public class TestAOP {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Waiter bean = context.getBean(Waiter.class);
        bean.showMenu();
        bean.pay();
    }
}
```

6. 测试结果

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727153122403-3dcbc716-1f7e-45d5-a32d-705ac1cef7d8.png)

<h4 id="urBd6">前置通知</h4>
```java
// 前置通知
@Before(value = "execution(* com.slz.aop..*.showMenu(..))")
public void doBefore(){
    System.out.println("欢迎光临");
    System.out.println("很高兴为您服务");
    System.out.println("-----------");
}
```

<h4 id="wGwxj">最终通知</h4>
`@After` 类似于`try...finally`，不管程序是否报错，都会执行到

```java
// 最终通知
@After(value = "execution(* com.slz.aop..*.pay(..))")
public void doAfter(){
    System.out.println("-----------");
    System.out.println("谢谢惠顾，下次再来");
}
```

<h4 id="S6Rtg">后置通知</h4>
1. `@AfterReturning`，在程序返回后执行，如果程序报错没有返回值，就不执行后置通知；
2. `@AfterReturning`在方法中可以传两个参数：
+ JoinPoint 参数：JoinPoint 接口提供了对这些连接点进行操作的能力；
+ Object 类型参数：作为接收目标对象方法的返回值；
3. 示例：`@<font style="color:#000000;">AfterReturning</font>(<font style="color:#ED740C;">value</font> = <font style="color:#74B602;">"execution(* login(..))")</font>, <font style="color:#ED740C;">returning</font>=<font style="color:#74B602;">"result"</font>)`，其中，<font style="color:#ED740C;">returning </font>与 Object 类型参数必须同名；
+ 如果返回的是基本数据类型和字符串，后置通知不能修改原方法返回的值；
+ 如果返回的是引用类型，后置通知可以对原方法返回的值进行修改；

```java
// 切点
public Boolean check(String code){
    System.out.println("请问您会员编号是多少");
    if("123456".equals(code))
        return true;
    return false;
}
```

```java
// 后置通知
@AfterReturning(value = "execution(* com.slz.aop..*.check(..))", returning = "result")
public void doAfterReturning(JoinPoint joinPoint, Object result){
    // 或取切点方法参数
    Object[] args = joinPoint.getArgs();
    // 判断切点方法返回值
    if((Boolean) result)
        for (Object arg : args) {
            System.out.println("编号：" + arg + "，是会员，享受优惠");
        }
    else {
        System.out.println("非会员");
    }
}
```

```java
public class 
{
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Waiter bean = context.getBean(Waiter.class);
        bean.showMenu();
        bean.check("123456");
        bean.pay();
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727156077019-affd8811-01ae-4f7d-8aff-41b02a379383.png)

<h4 id="PHz2o">环绕通知</h4>
`@Around`，适合用来做事务管理

+ 可在目标方法的<font style="color:#DF2A3F;">前面和后面做功能增强</font>，类似于 JDK 动态代理 InvocatlonHandler；
+ 可以通过程序的逻辑，<font style="color:#DF2A3F;">控制目标方法是否执行</font>；
+ 可以<font style="color:#DF2A3F;">修改目标方法最后返回的值</font>，不管是基本数据类型还是引用类型，都会被修改；

```java
// 切点
public String cook(String name){
    System.out.println("开始制作" + name);
    return "done";
}
```

```java
// 环绕通知
@Around(value = "execution(* com.slz.aop..*.cook(..))")
public String doAround(ProceedingJoinPoint point) {
    System.out.println("开启事务");
    String res = null;
    try {
        // 获取目标方法参数
        String arg = (String) point.getArgs()[0];
        if("重庆小面".equals(arg))
            // 获取目标方法执行返回值 ProceedingJoinPoint 接口继承自JoinPoint，用以调用目标方法
            res = (String) point.proceed();
        else System.out.println("做不了");
    } catch (Throwable e) {
        throw new RuntimeException(e);
    }
    System.out.println("结束事务");
    // 需要将目标方法返回值返回，以便于在主程序调用该方法时可以获取到返回值
    return res;
}
```

```java
public class TestAOP {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Waiter bean = context.getBean(Waiter.class);
        bean.showMenu();
        String res = bean.cook("重庆小面");
        System.out.println(res);
        bean.check("123456");
        bean.pay();
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727159362598-afb66ff8-85d2-4ff1-8e34-068157fdfdfd.png)

<h4 id="e69T7">异常通知</h4>
`@AfterThrowing`

+ 在目标方法抛出异常时，会进入到异常通知；
+ 如果目标方法不抛出异常，或将异常处理掉，则不会进入异常通知；

```java
// 切点
public Integer calculate(){
    try {
        return 10/0;
    } catch (ArithmeticException e){
        // 处理异常
        //            e.printStackTrace();
        // 抛出异常
        throw new RuntimeException(e);
    }
    //        return null;
}
```

```java
// 异常通知
@AfterThrowing(value = "execution(* com.slz.aop..*.calculate(..))", throwing = "e")
    public void doAfterThrowing(Exception e){
    System.out.println("出现异常" + e.toString());
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727161506284-c6777ee7-4da2-4695-a8fb-9052c69b758f.png)

<h4 id="snyqZ">Spring 对动态代理的选择</h4>
<h5 id="GINOa">❗getBean 获取 Bean 实例注意事项</h5>
> 实现了接口的类动态代理，`接口 bean = getBean(接口.class)`，如：
>
> `<font style="color:#ED740C;">UserDao</font> bean = context.<font style="color:#117CEE;">getBean</font>(<font style="color:#ED740C;">UserDao.class</font>);`
>

实现了接口的类，Spring 自动代理的默认方式是 JDK 方式：

1. 创建获取 Bean 实例对象时必须用接口接收：
    1. 因为相同接口功能相同，实现方式不同，自动代理<font style="background-color:#FBF5CB;">创建了新的类去代理目标类</font>；
2. getBean 传入参数也必须是接口：
    1. getBean 方法传入 <font style="color:#ED740C;">UserDao.class</font>，Spring会根据配置文件（如applicationContext.xml）中的定义<font style="background-color:#FBF5CB;">自动选择</font>一个实现类；
    2. 如果配置文件中有多个实现类，可以通过`@Qualifier`注解<font style="background-color:#FBF5CB;">指定具体实现类</font>；
3. 这样做的好处：
    1. 更好的**<font style="color:#DF2A3F;">解耦</font>**：使用接口类型可以使代码更易于维护和扩展；
    2. 更**<font style="color:#DF2A3F;">灵活的依赖注入</font>**：可以在运行时更容易地替换不同的实现；

<h5 id="KSccf">不同情况的选择</h5>
在 Spring 中使用 AOP 时，如果没有显式地禁用 CGLIB 代理，那么 Spring 会**<font style="color:#DF2A3F;">根据情况自动选择使用 CGLIB 或者 JDK 动态代理</font>**。

默认情况下：

+ 对**<font style="color:#DF2A3F;">未实现接口的类</font>**进行自动代理时 Spring 选择 **<font style="color:#DF2A3F;">CGLIB 动态代理</font>**

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727162235443-99d1f1f0-04cf-447c-8c6d-ec932d5bcaba.png)

+ 对**<font style="color:#DF2A3F;">实现了接口的类</font>**进行自动代理时 Spring 选择 **<font style="color:#DF2A3F;">JDK 动态代理</font>**

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727165424221-96e30909-6e0f-4443-aa77-5830a332b8fd.png)

+ Spring Bean配置文件配置如下：
    1. 当`<font style="color:#7E45E8;">proxy-target-class</font>=<font style="color:#74B602;">"true"</font>`时强制所有动态代理都使用CGLIB方式；
    2. 当`<font style="color:#7E45E8;">proxy-target-class</font>=<font style="color:#74B602;">"false"</font>`（默认）时，Spring视情况自动选择；

`<<font style="color:#5C8D07;">aop:aspectj-autoproxy</font> <font style="color:#7E45E8;">proxy-target-class</font>=<font style="color:#74B602;">"true"</font>><<font style="color:#5C8D07;">/aop:aspectj-autoproxy</font>>`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:aop="http://www.springframework.org/schema/aop"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd
  http://www.springframework.org/schema/aop
  https://www.springframework.org/schema/aop/spring-aop.xsd">
  <!--配置Bean扫描-->
  <context:component-scan base-package="com.slz.aop.*"></context:component-scan>
  <!-- 启用 AspectJ 的自动代理功能 -->
  <aop:aspectj-autoproxy proxy-target-class="true"></aop:aspectj-autoproxy>
</beans>
```

<h4 id="PyzWB">使用 XML 配置 AOP</h4>
> 很少用，了解即可
>

```xml
<!--    配置被代理bean-->
<bean id="waiter" class="com.slz.aop.aspect.UserDaoImpl"></bean>
<!--    配置代理bean-->
<bean id="myAdvice" class="com.slz.aop.aspect.MyAdvice"></bean>
<!--    配置aop-->
<aop:config>
  <!--        配置切点，切点表达式-->
  <aop:pointcut id="p" expression="execution(* com.slz.aop.aspect.*.save(..))"></aop:pointcut>
  <!--        配置切面-->
  <aop:aspect ref="myAdvice">
    <!--            配置通知/增强-->
    <aop:before method="doSave" pointcut-ref="p"></aop:before>
  </aop:aspect>
</aop:config>
```

<h4 id="OPtXM">@Pointcut 注解</h4>
该注解的作用是实现**<font style="color:#DF2A3F;">切点复用</font>**

```java
// 切点复用，共用切点提出，使用别名
@Pointcut(value = "execution(* com.slz.aop.aspect.*.save(..))")
public void PointCut1(){}

@Before(value = "PointCut1()")
public void doSave(){
    System.out.println("前置通知");
}
```

<h2 id="SDy1c">Spring 整合 Mybatis 框架</h2>
Mybatis 框架使用：

+ 需要mybatis全局的配置文件；
+ 需要对应的数据库、实体类、Mapper 接口和 Mapper.xml 配置文件；
+ 执行操作时，需要 SqlSessionFactory、SqlSession，生成 Mapper 的代理，执行 Mapper 中相关的方法；

使用 Spring 整合 Mybatis：

+ 把 Mybatis 框架中的关于数据库配置部分整合在 Spring 的容器中；
+ 把 SqlSessionFactory、SqlSession、Mapper、Service 整合在 Spring 容器中；

<h3 id="doXGv">准备工作</h3>
<h4 id="jun1e">配置 pom.xml 文件</h4>
**导入需要的依赖**

```xml
<dependencies>
  <!--    spring 上下文环境-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.15</version>
  </dependency>
  <!--    spring 事务-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>5.3.15</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.15</version>
  </dependency>
  <!-- druid 数据源 -->
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
  </dependency>
  <!-- mysql数据库驱动-->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
  </dependency>
  <!--lombok-->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.10</version>
  </dependency>
  <!--      mybatis-->
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.9</version>
  </dependency>
  <!--      mybatis 与 spring 整合-->
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.7</version>
  </dependency>
</dependencies>
```

**<font style="color:#DF2A3F;">设置资源文件打包路径</font>**：java目录下存放的都是java程序，其中资源文件默认是不会编译到target里的，需要配置打包路径（这里就不配了），这里统一将资源文件存放在 resources 下，配置扫描该路径下的所有.properties 和 .xml 资源文件，并编译打包进 target 目录，以便于编译后的java程序，即.class文件可以找到相应的资源文件。

```xml

<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <includes>
        <include>**/*.properties</include>
        <include>**/*.xml</include>
      </includes>
      <filtering>false</filtering>
    </resource>
  </resources>
</build>
```

<h4 id="Q86en">配置 mybatis 配置文件</h4>
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <!--    对数据库环境配置的部分整合在Spring容器中来完成， 这里不需要配置-->
  <!--    <environments default="dev">-->
  <!--        <environment id="dev">-->
  <!--            <transactionManager type="JDBC"></transactionManager>-->
  <!--            <dataSource type="POOLED">-->
  <!--                <property name="driver" value="${mysql.driver}"/>-->
  <!--                <property name="url" value="${mysql.url}"/>-->
  <!--                <property name="username" value="${mysql.user}"/>-->
  <!--                <property name="password" value="${mysql.password}"/>-->
  <!--            </dataSource>-->
  <!--        </environment>-->
  <!--    </environments>-->
  <typeAliases>
    <package name="com.slz.mybatis.entity"/>
  </typeAliases>
  <mappers>
    <package name="com.slz.mybatis.mapper"/>
  </mappers>
</configuration>
```

<h4 id="T4pC4">配置 db.properties 文件</h4>
```properties
# driver is not necessary, because it can find driver by url
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/Mybatis?useSSL=false&serverTimezone=GMT%2B8
jdbc.username=root
jdbc.password=root
```

<h4 id="fNhtn">配置 Spring 配置文件</h4>
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
  https://www.springframework.org/schema/context/spring-context.xsd">
  <!--    加载dp.properties文件到容器-->
  <context:property-placeholder location="classpath:dp.properties"></context:property-placeholder>
  <!--    自动扫描bean-->
  <context:component-scan base-package="com.slz.mybatis"></context:component-scan>
  <!--    数据源配置-->
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
    <property name="url" value="${jdbc.url}"></property>
    <property name="username" value="${jdbc.username}"></property>
    <property name="password" value="${jdbc.password}"></property>
  </bean>
  <!--    配置 SqlSessionFactory -->
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"></property>
    <property name="configLocation" value="classpath:mybatisConfig.xml"></property>
    <!--        配置 mapper.xml 文件位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
  </bean>
  <!--    对 Mapper 的扫描-->
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
    <!--        配置 mapper 的基准包-->
    <property name="basePackage" value="com.slz.mybatis.mapper"></property>
  </bean>
</beans>
```

<h3 id="T1giX">应用示例</h3>
![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727239563815-ee2dad77-389a-40b4-8de5-6b7a4f8ccdf9.png)

1. 实体类

```java
@Data
@Accessors(chain = true)
public class Student {
    private int id;
    private String name;
    private int age;
    private String gender;
}
```

2. mapper 层和 mapper.xml `@Repository`

```java
// 虽然 mapper 是接口，用注解标注为组件后，Spring 容器回自动代理生成实现类
@Repository
public interface StudentMapper {
    void save(Student student);
    List<Student> selectList();
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.slz.mybatis.mapper.StudentMapper">
  <insert id="save" parameterType="Student">
    insert into student values (null, #{name}, #{age}, #{gender})
  </insert>
  <select id="selectList" resultType="Student">
    select * from student
  </select>
</mapper>
```

3. service 层 `@Service`

```java
public interface StudentService {
    void save(Student student);
    List<Student> selectList();
}
```

```java
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper mapper;
    @Override
    public void save(Student student) {
        mapper.save(student);
    }

    @Override
    public List<Student> selectList() {
        return mapper.selectList();
    }
}
```

4. 测试

```java
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = new Student().setName("张三").setAge(24).setGender("男");
        StudentService bean = context.getBean(StudentService.class);
        //        bean.save(student);
        List<Student> students = bean.selectList();
        students.forEach(System.out::println);
    }
}
```

<h2 id="oNoli">Spring 事务管理</h2>
+ **使用 jdbc 技术来实现事务管理**：设置自动提交为 false， 当没有异常，手动提交commit，如果有异常，调用 rollback；
+ **使用 mybatis 来实现事务管理**：设置自动提交为 false，sqlsession 来调用commit， rollback；
+ **使用 spring 框架**：屏蔽掉不同数据库操作方式带来的差异，提供统一的事务管理机制；

⭐ spring 框架内部使用事务管理器的接`PlatformTransactionManager`，来完成commit 和 rollback：

+ 针对不同的框架，提供不同的实现类：
    - mybatis 框架：提供实现类 `DataSourceTransactionManager`；
    - hibernate 框架：提供实现类 `HibernateTransactionManager`；

<h3 id="oSQxE">事务隔离级别 - 6 种</h3>
在 Spring 框架中，事务隔离级别是通过`Isolation`枚举来定义的，它主要用于控制事务的并发行为，以解决事务中的常见问题，如脏读、不可重复读和幻读等。Spring支持以下几种隔离级别：

| 序号 | 隔离级别 | 描述 | 脏读 | 不可重复读 | 幻读 |
| --- | --- | --- | --- | --- | --- |
| 1 | ISOLATION_DEFAULT | 数据库默认级别：<br/>+ mysql 是可重复读；<br/>+ oracle 是读已提交； | - | - | - |
| 2 | ISOLATION_NONE | 不使用事务 | - | - | - |
| 3 | ISOLATION_READ_UNCOMMITTED | 读未提交 | **<font style="color:#DF2A3F;">Y</font>** | **<font style="color:#DF2A3F;">Y</font>** | **<font style="color:#DF2A3F;">Y</font>** |
| 4 | ISOLATION_READ_COMMITTED | 读已提交 | **<font style="color:#74B602;">N</font>** | **<font style="color:#DF2A3F;">Y</font>** | **<font style="color:#DF2A3F;">Y</font>** |
| 5 | ISOLATION_REPEATABLE_READ | 可重复读 | **<font style="color:#74B602;">N</font>** | **<font style="color:#74B602;">N</font>** | **<font style="color:#DF2A3F;">Y</font>** |
| 6 | ISOLATION_SERIALIZABLE | 串行化 | **<font style="color:#74B602;">N</font>** | **<font style="color:#74B602;">N</font>** | **<font style="color:#74B602;">N</font>** |


<h3 id="TT00N">事务超时时间 - 7 种</h3>
事务超时时间：表示当前方法中事务最长的执行时间，以秒为单位计时，默认 -1，不限时。

+ 如果在执行时间内结束，则事务正常提交；
+ 如果超过指定时间，则事务回滚；

<h3 id="XUTtI">事务传播行为 ⭐ </h3>
事务传播行为：设置在调用方法时，是否要加入事务管理，使用什么样的事务管理机制。一共分为七种：

| 序号 | 传播行为 | 描述 | 适用场景 |
| --- | --- | --- | --- |
| 1 | **PROPAGATION_REQUIRED** | 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务 | 大多数业务逻辑方法都应该使用此传播行为 |
| 2 | **PROPAGATION_SUPPORTS** | 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务方式执行 | 用于读取操作或其他不需要事务的操作 |
| 3 | **PROPAGATION_MANDATORY** | 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常 | 用于必须在事务上下文中执行的方法 |
| 4 | PROPAGATION_REQUIRES_NEW | 总是创建一个新的事务，并且挂起当前事务（如果有） | 用于独立的事务操作，与外部事务隔离 |
| 5 | PROPAGATION_NOT_SUPPORTED | 以非事务方式执行操作，并且挂起当前事务（如果有） | 用于不需要事务的操作，但希望在事务外部执行 |
| 6 | PROPAGATION_NEVER | 以非事务方式执行操作，并且如果当前存在事务，则抛出异常 | 用于明确禁止事务的操作 |
| 7 | PROPAGATION_NESTED | 如果当前存在事务，则在嵌套事务内执行；如果当前没有事务，则行为类似于PROPAGATION_REQUIRED | 用于支持嵌套事务的场景，如保存点 |


`REQUIRED` / `REQUIRES_NEW` / `NESTED`三者的区别：

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727243462875-970c21c9-937b-42c8-bd05-4827a0c5308e.png)

<h3 id="fZOO6">事务提交和回滚的时机</h3>
+ 如果业务方法可以**正常执行**结束，没有异常，事务执行提交；
+ 如果方法中出现**运行时异常**或者是 **Error**，则回滚事务；
+ 如果方法中出现**检查型异常**，则提交事务；

<h3 id="ZQbiV">Spring 事务配置</h3>
<h4 id="eCpue">准备</h4>
1. 场景

一个用户，银行帐户，执行一笔支付操作，将用户余额减少，并在交易记录中记录一条数据；

2. 数据库表结构
    1. 银行账户表

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727244503220-09c51d1a-1b8f-44c3-8b73-56317cc848c6.png)

    2. 交易记录表

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727244664310-39f18e19-ddfe-4987-bf57-05698b2b1982.png)

3. 实体类

```java
@Data
@Accessors(chain = true)
public class Acc {
    private int id;
    private String accno;
    private String name;
    private int money;
}
```

```java
@Data
@Accessors(chain = true)
public class Records {
    private int id;
    private String accno;
    private String category;
    private int money;
}
```

4. mapper层

```java
@Repository
public interface AccMapper {
    @Update("update acc set money=money-#{money} where accno=#{accno}")
    void update(Acc acc);
}
```

```java
@Repository
public interface RecordsMapper {
    @Insert("insert into  records values (null, #{accno}, #{category}, #{money})")
    void insert(Records records);
}
```

5. service 层

```java
public interface AccService {
    void doPay(int money);
}
```

```java
@Service
public class AccServiceImpl implements AccService {
    @Resource
    private AccMapper accMapper;
    @Resource
    private RecordsMapper recordsMapper;
    @Override
    public void doPay(int money) {
        // 存一条交易记录
        Records records = new Records().setAccno("123456").setCategory("支出").setMoney(money);
        recordsMapper.insert(records);
        System.out.println("执行交易记录处理");
        // 修改用户原账户下的余额
        Acc acc = new Acc().setAccno("123456").setMoney(money);
        accMapper.update(acc);
        System.out.println("执行用余额修改处理");
    }
}
```

<h4 id="LNq9R">注解配置</h4>
<h5 id="gi2cC">应用示例</h5>
+ 对事务管理的注解，一般要**<font style="color:#74B602;">写在 service 层的方法</font>**上，因为业务层的逻辑才会比较复杂；
+ 底层运用动态代理的机制，所以要求，被注解的**<font style="color:#74B602;">方法是 public 的方法</font>**；
1. 在 spring 配置文件配置事务

```xml
<!--    配置事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <!--        指定数据源-->
  <property name="dataSource" ref="dataSource"></property>
</bean>
<!--    开启事务管理-->
<tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
```

2. 在开启事务的方法上加入注解 `<font style="color:#DF2A3F;">@Transactional</font>`

```java
@Transactional
@Override
public void doPay(int money) {
    // 存一条交易记录
    Records records = new Records().setAccno("123456").setCategory("支出").setMoney(money);
    recordsMapper.insert(records);
    System.out.println("执行交易记录处理");
    // 修改用户原账户下的余额
    Acc acc = new Acc().setAccno("123456").setMoney(money);
    accMapper.update(acc);
    System.out.println("执行用余额修改处理");
}
```

3. 事务处理

```java
@Service
public class AccServiceImpl implements AccService {
    @Resource
    private AccMapper accMapper;
    @Resource
    private RecordsMapper recordsMapper;

    @Transactional
    @Override
    public void doPay(int money) {
        // 存一条交易记录
        Records records = new Records().setAccno("123456").setCategory("支出").setMoney(money);
        recordsMapper.insert(records);
        System.out.println("执行交易记录处理");
        //-------------------------- 异常回滚情况-------------------

        // 一、运行时异常
        try {
            int x = 10 / 0;
        } catch (ArithmeticException e){
            e.printStackTrace(); // 1. 如果异常被处理，则不会回滚
            //            throw new RuntimeException(e); // 2. 如果抛出异常，则会回滚
        }
        //        int x = 10 / 0; // 3. 程序异常而中止，会回滚
        // 二、检查时异常
        //        throw new FileNotFoundException("文件找不到"); // 检查型异常，不会回滚

        //----------------------------------------------------------
        // 修改用户原账户下的余额
        Acc acc = new Acc().setAccno("123456").setMoney(money);
        accMapper.update(acc);
        System.out.println("执行用余额修改处理");
    }
}
```

4. 测试

```java
public class TestTrans {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccService bean = context.getBean(AccService.class);
        bean.doPay(70);
    }
}
```

<h5 id="CiCRh">@Transactional 注解使用详解</h5>
```java
@Transactional(
    propagation = Propagation.REQUIRED, // 配置事务传播机制
    isolation = Isolation.REPEATABLE_READ, // 配置事务隔离机制
    timeout = 10, // 配置超时时间，超时回滚
    rollbackFor = { // 配置回滚异常
        FileNotFoundException.class // 加入回滚后，即使检查型异常也会回滚
    },
    noRollbackFor = { // 配置不回滚异常
        NullPointerException.class
    }
)
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727257374996-909948b3-1940-4055-ba0d-4c717d53d628.png)

<h4 id="oaFEc">XML配置</h4>
> XML 配置通过 AOP 完成
>

1. pom.xml 文件导入依赖 spring-aspects

```xml
<!--      spring-aspects-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aspects</artifactId>
  <version>5.3.16</version>
</dependency>
```

2. 配置 Spring 配置文件，applicationContext.xml

```xml
<!--    配置事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <!--        指定数据源-->
  <property name="dataSource" ref="dataSource"></property>
</bean>
<!--    开启注解方式事务管理, 不需要注解方式配置可以注释掉-->
<!--    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>-->
<!--    xml 方式事务管理配置-->
<tx:advice id="interceptor" transaction-manager="transactionManager">
  <tx:attributes>
    <!--            配置添加事务管理的方法-->
    <tx:method name="doPay" propagation="REQUIRED" timeout="10" isolation="DEFAULT"/>
    <!--            支持通配符写法-->
    <tx:method name="do*" propagation="REQUIRED" isolation="READ_COMMITTED"/>
    <tx:method name="*" propagation="REQUIRED"/>
  </tx:attributes>
</tx:advice>
<!--    配置 AOP，将开启事务管理的方法通过切点表达式切入 -->
<aop:config>
  <aop:pointcut id="myPointcut" expression="execution(* *..service..*.*(..))"/>
  <aop:advisor advice-ref="interceptor" pointcut-ref="myPointcut"></aop:advisor>
</aop:config>
```

<h2 id="T9Ek4">SpringMVC</h2>
> MVC框架演变：Struts -> Struts2 -> SpringMVC -> SpringBoot
>

![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/42892034/1727326078503-a00bf250-0435-4658-934d-55513374d985.jpeg)

<h3 id="cyCtW">创建一个 Web 工程</h3>
1. 创建一个Web工程

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727326858203-8f00967e-333d-4e53-978a-226f4daedfd6.png)

2. 配置 pom.xml 文件

```xml
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>3.8.1</version>
    <scope>test</scope>
  </dependency>
  <!--    servlet api-->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
    <scope>provided</scope>
  </dependency>
  <!--    jsp api-->
  <dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
    <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
    <scope>provided</scope>
  </dependency>
</dependencies>
```

3. 创建 Servlet 程序，路径` com.slz.springmvc.servlet.TestServlet`

```java
public class TestServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter writer = res.getWriter();
        writer.print("hello");
    }
}
```

4. 配置 web.xml 文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaeehttp://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
  version="4.0">
</web-app>
```

5. 在 Servlet 上加入注解，标明 Servlet 访问路径

```java
@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter writer = res.getWriter();
        writer.print("hello");
    }
}
```



6. 将工程发布在 tomcat 服务器上

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727349822881-4b8560f0-de18-423f-8954-8ddd139934f3.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727349853138-f8b35f24-2ecf-4836-85b4-30bb6b617390.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350022182-c45e1493-b0fd-4c97-87f5-e4d858c90d13.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350032119-339792f5-d673-4a8f-bada-8823080fa192.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350035800-a908bf1c-19c8-4f0e-9667-c848665854f4.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350097150-9704bfcd-dddb-4d65-80dc-c3084a83804a.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350194097-26cad5ce-0991-47b0-a9a6-2badcd103482.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350819808-cbf04072-7b24-42ba-9c18-581c3d56bea6.png)

<h3 id="R4zSh">SpringMVC 工作流程</h3>
![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727350918623-dde8480f-ef16-4654-97b5-2985952caa12.png)

> Dispatcher：调度器
>

1. `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>对 url 进行解析匹配，匹配到对应的 Servlet 去处理，这里的 Servlet 叫做 Handler；
2. `<font style="color:#DF2A3F;">HandlerMapping</font>`<font style="color:#DF2A3F;"> </font>根据匹配的 Handler 返回给 `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>对应的拦截器，执行链；
3. `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>请求不同的适配器 `<font style="color:#DF2A3F;">HandlerAdapter</font>`<font style="color:#DF2A3F;"> </font>执行；
4. `<font style="color:#DF2A3F;">HandlerAdapter</font>`<font style="color:#DF2A3F;"> </font>交给对应的 `<font style="color:#DF2A3F;">Handler</font>`<font style="color:#DF2A3F;"> </font>处理器执行；
5. `<font style="color:#DF2A3F;">Handler</font>`<font style="color:#DF2A3F;"> </font>将执行的结果（模型和视图，ModelAndView 返回给`<font style="color:#DF2A3F;">HandlerAdapter</font>`<font style="color:#DF2A3F;"> </font>）；
6. `<font style="color:#DF2A3F;">HandlerAdapter</font>`<font style="color:#DF2A3F;"> </font>再将 ModelAndView 返回给 `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>；
7. `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>请求 `<font style="color:#DF2A3F;">View Resolver</font>`进行视图解析；
8. `<font style="color:#DF2A3F;">View Resolver</font>` 将解析后的视图返回给 `<font style="color:#DF2A3F;">DispatcherServlet</font>`<font style="color:#DF2A3F;"> </font>；
9. `<font style="color:#DF2A3F;">DispatcherServlet</font>`将模型数据填入 request，并将 reponse 响应返回给用户；

> 除了处理器（Handler / Servlet）和视图（jsp / freemarket / excel / jpg）需要自己编写程序，其它流程只需要配置；
>

<h3 id="buSp5">SpringMVC 项目入门</h3>
<h4 id="LxJvb">配置前端控制器、处理器适配器和映射器</h4>
> + **<font style="color:#DF2A3F;">前端控制器 </font>**`<font style="color:#DF2A3F;">DispatcherServlet</font>`
> + **<font style="color:#DF2A3F;">处理器适配器 </font>**`<font style="color:#DF2A3F;">HandlerAdapter</font>`
> + **<font style="color:#DF2A3F;">处理器映射器 </font>**`<font style="color:#DF2A3F;">HandlerMapping</font>`
>

1. 创建 web 工程；
2. 配置 pom.xml，引入 spring-webmvc 依赖；

> **<font style="color:#DF2A3F;">注意</font>**：spring-webmvc 依赖引入后，包含了spring-context等依赖，不需要重复导入依赖：
>
> ![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727353738348-24b8e9fd-db18-4058-86a5-09e6d9151d64.png)
>

```xml
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>3.8.1</version>
    <scope>test</scope>
  </dependency>
  <!--    servlet api-->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
    <scope>provided</scope>
  </dependency>
  <!--    jsp api-->
  <dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
    <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
    <scope>provided</scope>
  </dependency>
  <!--    lombok-->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.22</version>
  </dependency>
  <!--    spring-mvc-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.15</version>
  </dependency>
</dependencies>
```

3. 创建 springmvc 配置文件 springmvc.xml 

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727354676307-cfe6474d-f092-4cd2-88eb-c7756ae9f0cc.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

4. 配置 web.xml，**<font style="color:#DF2A3F;">配置前端控制器 </font>**`<font style="color:#DF2A3F;">DispatcherServlet</font>`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaeehttp://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
  version="4.0">
  <!--    配置前端控制器 DispatcherServlet-->
  <servlet>
    <!--      servlet-name 可以自定义  -->
    <servlet-name>mvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--        配置初始化参数，指定 springmvc 配置文件的位置-->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>mvc</servlet-name>
    <!--        / 拦截所有，包括静态资源 js css，不包括 jsp-->
    <url-pattern>/</url-pattern>
    <!--        /* 拦截所有，包括 jsp-->
    <!--        <url-pattern>/*</url-pattern>-->
  </servlet-mapping>
</web-app>
```

5. 配置  springmvc.xml ，**<font style="color:#DF2A3F;">配置处理器映射器</font>**`<font style="color:#DF2A3F;">HandlerMapping</font>`**<font style="color:#DF2A3F;">和处理器适配器</font>**`<font style="color:#DF2A3F;">HandlerAdapter</font>`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
  <!--   配置 Spring Bean 组件扫描 -->
  <context:component-scan base-package="com.slz.springmvc"></context:component-scan>
  <!-- 配置处理器适配器和处理器映射器, 下面一行代码就够了-->
  <mvc:annotation-driven></mvc:annotation-driven>
</beans>
```

<h4 id="mHqjm">编写处理器和视图</h4>
> + **<font style="color:#DF2A3F;">处理器</font>** `<font style="color:#DF2A3F;">Handler</font>`
> + **<font style="color:#DF2A3F;">视图</font>**<font style="color:#DF2A3F;"> </font>`<font style="color:#DF2A3F;">View</font>`
>

1. 编写 Controller 类 `<font style="color:#DF2A3F;">Handler</font>`

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727357134287-3be0072f-a27b-4f78-8e57-984f3e4a3a4e.png)

```java
/**
 * 相当于 Servlet，不需要继承 HttpServlet
 */

@Controller // 相当于 @Component
public class StudentController {
    @RequestMapping("stuSelect")
    public ModelAndView selectList(){
        List<String> list = Arrays.asList("AA", "BB", "CC");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test.jsp"); // 要跳转的地址
        modelAndView.addObject("list", list); // req.setAttribute("list")
        return modelAndView;
    }
    @RequestMapping("stuSave")
    public ModelAndView save(){
        return null;
    }
}
```

> 上述代码相当于 Servlet，与如下程序功能相同
>

```java
@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        List<String> list = Arrays.asList("AA", "BB", "CC");
        req.setAttribute("list", list); // 设置模型数据 req.setAttribute("list")
        req.getRequestDispatcher("test.jps").forward(req, res); // 设置视图 要跳转的地址
    }
}
```

2. 编写 jsp `<font style="color:#DF2A3F;">View</font>`

```javascript
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        List<String> list =  (List<String>) request.getAttribute("list");
        for (String str : list) {
            out.print(str + "<br>");
        }
    %>
</body>
</html>

```

3. 启动 tomcat 访问 [http://localhost:8080/SpringMvc_war_exploded/stuSelect](http://localhost:8080/SpringMvc_war_exploded/stuSelect)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727412427496-532900a1-81db-40bc-86c1-0ee43ecb7295.png)

<h4 id="eTlNY">配置视图解析器</h4>
spring 配置文件下配置 bean，springmvc.xml

```xml
<!--    配置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
```

这里配置了前端页面访问路径` /WEB-INF/pages`，配置了前端页面的文件后缀，因此访问前端页面时可以直接使用不带文件后缀的文件名即可：

```java
@RequestMapping("loginSubmit")
public String loginSubmit(String name, String password) { // spring mvc 可以直接从前端获取参数
    if ("admin".equals(name) && "123456".equals(password))
        return "success"; // 配置了视图解析器，可以直接返回文件名，进行页面跳转
    return "fail";
}
```

> `<font style="color:#DF2A3F;">@RequestMapping</font>` 注解可以放在方法和类前面，放在类的前面，表示访问 url 的前缀；
>
> ![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727415433341-133c070f-d7c3-4a96-b8ca-297af501572f.png)
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727441995685-b180de70-e185-4f8b-8d5a-ee5417bb6383.png)

<h4 id="SjgsR">接收视图层提交参数</h4>
1. 创建 jsp，login.jsp / success.jsp / fail.jsp，创建路径：src/main/webapp/WEB-INF/pages/

```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
    <head>
      <title>login</title>
    </head>
    <body>
      <form action="loginSubmit" method="post">
        <div>
          账号：<input type="text" name="name">
            密码：<input type="password" name="password">
            </div>
        <div>
          <button type="submit">登录</button>
        </div>
      </form>
    </body>
  </html>
```

```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
    <head>
      <title>Success</title>
    </head>
    <body>
      login success !!!
    </body>
  </html>
```

```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
    <head>
      <title>Fail</title>
    </head>
    <body>
      login fail
    </body>
  </html>
```

2. Controller 处理

```java
@Data
public class Users {
    private String name;
    private String password;
}
```

```java
@RequestMapping("login")
public String login() {
    return "login";
}
@RequestMapping("loginSubmit")
public String loginSubmit(String name, String password) { // spring mvc 可以直接从前端获取参数
    if ("admin".equals(name) && "123456".equals(password))
        return "success"; // 配置了视图解析器，可以直接返回文件名，进行页面跳转
    return "fail";
}
@RequestMapping("loginSubmit2")
public String loginSubmit(Users users) { // 从前端接收的参数，可以被自动封装为类对象
if ("admin".equals(users.getName()) && "123456".equals(users.getPassword()))
    return "success";
return "fail";
}
```

> <font style="color:#DF2A3F;">从前端接收的参数，可以被自动封装为类对象</font>
>

**<font style="color:#DF2A3F;">注意</font>**<font style="color:#DF2A3F;">：</font>

+ <font style="color:#DF2A3F;">SpringMVC可以从前端直接获取参数，无论是基本类型/字符串还是封装成类对象；</font>
+ <font style="color:#DF2A3F;">而一般 Servlet 方式，需要获取接收 </font>`<font style="color:#74B602;">HttpServletRequest request</font>` <font style="color:#DF2A3F;">参数，通过</font> `r<font style="color:#74B602;">equest.getParameter("name")</font>`<font style="color:#DF2A3F;">获取前端传来的参数</font>；

```java
public String loginSubmit(HttpServletRequest request){
String name = request.getParameter("name");
String password = request.getParameter("password");
return null;
}
```

<h4 id="GEcH0">静态资源管理</h4>
> 静态资源：css、js、image 等；jsp 是动态
>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727442045298-b2a1d821-4e1a-4feb-9604-ea144ce15837.png)

1. 示例：在 jsp 中加载静态资源 css

```css
div{
  width: 100px;
  height: 100px;
  border: 1px solid red;
}
```

```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
    <head>
      <title>Home</title>
      <link rel="stylesheet" href="static/css/web.css">
    </head>
    <body>
      <div>我是老六</div>
    </body>
  </html>
```

1. web.xml 中配置了 servlet 映射规则，会拦截静态资源

```xml
<servlet-mapping>
  <servlet-name>mvc</servlet-name>
  <!--        / 拦截所有，包括静态资源 js css，不包括 jsp-->
  <url-pattern>/</url-pattern>
  <!--        /* 拦截所有，包括 jsp-->
  <!--        <url-pattern>/*</url-pattern>-->
</servlet-mapping>
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727417962995-8b86cd06-886a-40bb-8307-eb4aa483e14a.png)

2. 所以需要配置静态资源放行，在 spring bean配置文件 springmvc.xml 中配置；

```xml
<!--   设置静态资源放行，指定路径和地址 -->
<mvc:resources mapping="/static/**" location="/static/"></mvc:resources>
```

3. 再次访问 home.jsp

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727419621725-7b493c06-6e28-4d0f-b209-810d3291cfd5.png)

<h4 id="iZAeA"><font style="color:#DF2A3F;">@RequestMapping 详解</font></h4>
> + `@PostMapping`
> + `@GetMapping`
>

1. servlet 一般程序做查询处理：
    1. 使用 get 提交，在 servlet 里会使用 doGet 方法做响应；
    2. 使用 post 提交，在 servlet 里会使用 doPost 方法做响应；
2. springmvc 如果不做限定，get / post 方式都可以，可以使用注解作限定
    1. 限定方法只能使用 get 方式访问

`<font style="color:#ECAA04;">@RequestMapping</font>(value = <font style="color:#74B602;">"view"</font>, method = RequestMethod.<font style="color:#601BDE;">GET</font>)`<font style="color:#ECAA04;"> </font>或者

`<font style="color:#ECAA04;">@GetMapping</font>(<font style="color:#74B602;">"view"</font>)`

    2. 限定方法只能使用 post 方式访问

`<font style="color:#ECAA04;">@RequestMapping</font>(value = <font style="color:#74B602;">"view"</font>, method = RequestMethod.<font style="color:#601BDE;">POST</font>)`<font style="color:#ECAA04;"> </font>或者

`<font style="color:#ECAA04;">@PostMapping</font>(<font style="color:#74B602;">"view"</font>)`

3. 此外，还有注解

```java
@PutMapping // 执行修改方法，使用 put 方法提交
@DeleteMapping // 执行删除方法，使用 delete 方法提交
```

<h3 id="yBk1u">RESTFul 风格支持</h3>
<h4 id="Aoycc">简介</h4>
[什么是REST风格? 什么是RESTFUL?（一篇全读懂）_rest风格和restful风格-CSDN博客](https://blog.csdn.net/SeniorShen/article/details/111591122)

REST之所以晦涩难懂，是因为前面主语（Resource ）被去掉了。

全称是： Resource Representational State Transfer。

通俗来讲就是：**<font style="color:#E4495B;">资源在网络中以某种</font>****<font style="color:#E4495B;background-color:#FBDE28;">表现形式</font>****<font style="color:#E4495B;">进行</font>****<font style="color:#E4495B;background-color:#FBDE28;">状态转移</font>**。

> 分解开来讲解：
>
> + Resource：资源，即数据（这是网络的核心）；
> + Representational：某种表现形式，比如用 JSON，XML，JPEG 等；
> + State Transfer：状态变化，通过 HTTP 的动词（**<font style="color:#DF2A3F;">get查询、post新增、put修改、delete删除</font>**）实现。
>

<font style="color:rgb(77, 77, 77);">REST 风格的接口有什么好处呢：</font>**<font style="color:#ED740C;">前后端分离</font>**

> + **前端**拿到数据只负责展示和渲染，不对数据做任何处理。
> + **后端**处理数据并以**<font style="color:#DF2A3F;"> JSON 格式</font>**传输出去，定义这样一套统一的接口，在 web，ios，android 三端都可以用相同的接口，是不是很爽？！（因为不需要写三次代码，一次代码可以公用给三端；另外，修改代码只要修改一次，三端都同步访问新代码，不需要修改三次代码。）
> + <font style="color:rgb(0, 0, 0);background-color:#FBDE28;">无状态约束。是他的一个缺点。</font>
>

<font style="color:rgb(77, 77, 77);">REST 风格URL传递的参数没有明确标识</font>

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727422967250-c2846049-fbcb-4d5c-a0ba-3ee094ff9be0.png)

<h4 id="rR6QK">应用实例</h4>
`<font style="color:#E4495B;">@PathVariable("id")</font>`表示从URL路径中获取传递的参数

```java
@RequestMapping("delete/{id}")
public String delete(@PathVariable("id") String sid){
    System.out.println(sid);
    return "get";
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727422679204-cfe7ffb2-5d1d-485c-9989-00b3f8b4814f.png)

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727422787780-0da6640f-3a87-48d1-9b77-2f629b67ce5d.png)

<h3 id="xoz6m">SSM 框架整合</h3>
![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727503791785-e3753234-2c0a-4c38-bd6b-6d159b7286fd.png)

<h4 id="eOGDT">整合步骤</h4>
1. 创建一个 Maven Web 工程
2. 在 pom.xml 文件中加入相关依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.slz.springmvc</groupId>
  <artifactId>SpringMvc</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>SpringMvc Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <!--    1. servlet api-->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
      <scope>provided</scope>
    </dependency>
    <!--    2. jsp api-->
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.3</version>
      <!--      tomcat 中包含该包，这里只是提供使用，provided 仅在编译阶段有效，打包和运行时不包含该依赖-->
      <scope>provided</scope>
    </dependency>
    <!--    3. lombok-->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.22</version>
    </dependency>
    <!--   4. spring-mvc-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.15</version>
    </dependency>
    <!--    5. spring 事务-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.3.15</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.3.15</version>
    </dependency>
    <!-- 6. druid 数据源 -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.8</version>
    </dependency>
    <!-- 7. mysql数据库驱动-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.28</version>
    </dependency>
    <!--  8. mybatis-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.9</version>
    </dependency>
    <!--   9. mybatis 与 spring 整合-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>2.0.7</version>
    </dependency>
  </dependencies>
  <!--    资源文件打包路径-->
  <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
</project>
```

3. 创建相关的配置文件
    1. 数据库相关属性文件 db.properties

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://8.130.102.188:3306/Mybatis?useSSL=false&serverTimezone=GMT%2B8
jdbc.username=root
jdbc.password=root
```

    2. mybatis 相关配置文件 mybatisConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--    配置实体类别名-->
    <typeAliases>
        <package name="com.slz.springmvc.entity"/>
    </typeAliases>
    <!--    配置mapper基准包-->
    <mappers>
        <package name="com.slz.springmvc.mapper"/>
    </mappers>
</configuration>
```

    3. spring 相关配置文件 applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">
    <!--    加载dp.properties文件到容器-->
    <context:property-placeholder location="classpath:dp.properties"></context:property-placeholder>
    <!--    数据源配置-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <!--    配置 SqlSessionFactory -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"></property>
        <property name="configLocation" value="classpath:mybatisConfig.xml"></property>
        <!--        配置 mapper.xml 文件位置-->
        <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
    </bean>
    <!--    对 Mapper 的扫描-->
    <bean id="scannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
        <!--        配置 mapper 的基准包-->
        <property name="basePackage" value="com.slz.springmvc.mapper"></property>
    </bean>
</beans>
```

    4. springmvc 相关配置文件 springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
  <!--   配置 Spring Bean 组件扫描 -->
  <context:component-scan base-package="com.slz.springmvc"></context:component-scan>
  <!-- 配置处理器适配器和处理器映射器, 下面一行代码就够了-->
  <mvc:annotation-driven></mvc:annotation-driven>
  <!--    配置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
  <!--   设置静态资源放行，指定路径和地址 -->
  <mvc:resources mapping="/static/**" location="/static/"></mvc:resources>
</beans>
```

    5. web.xml 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--    Spring 配置-->
    <!--    监听器，用于加载 Spring 容器上下文环境，ApplicationContext-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!--    配置 spring 文件的地址-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <!--    SpringMVC 配置-->
    <!--    配置前端控制器 DispatcherServlet-->
    <servlet>
        <!--      servlet-name 可以自定义  -->
        <servlet-name>mvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--        配置初始化参数，指定 springmvc 配置文件的位置-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>mvc</servlet-name>
        <!--        / 拦截所有，包括静态资源 js css，不包括 jsp-->
        <url-pattern>/</url-pattern>
        <!--        /* 拦截所有，包括 jsp-->
        <!--        <url-pattern>/*</url-pattern>-->
    </servlet-mapping>
</web-app>

```

4. 编写entity、mapper、service、controller

```java
@Data
public class Student {
    private int id;
    private String name;
    private int age;
    private String gender;
}
```

```java
@Repository
public interface StudentMapper {
    void save(Student student);
    List<Student> selectList();
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.slz.springmvc.mapper.StudentMapper">
  <insert id="save">
    insert into student values (null, #{name}, #{age}, #{gender})
  </insert>
  <select id="selectList" resultType="Student">
    select * from student;
  </select>
</mapper>
```

```java
public interface StudentService {
    void save(Student student);
    List<Student> selectList();
}
```

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Override
    public void save(Student student) {
        studentMapper.save(student);
    }

    @Override
    public List<Student> selectList() {
        return studentMapper.selectList();
    }
}
```

```java
@Controller
@RequestMapping("student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("select")
    public String select(HttpServletRequest request){
        List<Student> students = studentService.selectList();
        request.setAttribute("list", students);
        return "list";
    }

    @PostMapping("insert")
    public String save(Student student){
        studentService.save(student);
        // 重定向
        return "redirect:select";
    }
}
```

5. 编写  jsp

```xml
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
    <head>
      <title>Save</title>
    </head>
    <body>
      <form action="/student/insert" method="post">
        <div>姓名：<input type="text" name="name"></div>
        <div>年龄：<input type="number" name="age"></div>
        <div>性别：
          <input type="radio" name="gender" value="男">男
            <input type="radio" name="gender" value="女">女
            </div>
        <div><button type="submit">提交</button></div>
      </form>
    </body>
  </html>
```

```xml
<%@ page import="com.slz.springmvc.entity.Student" %>
  <%@ page import="java.util.List" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
        <head>
          <title>List</title>
          <style>
            table, td {
            border: 1px solid black;
            border-collapse: collapse;
            }
            table {
            margin: 0 auto;
            text-align: center;
            width: 700px;
            }
          </style>
        </head>
        <body>
          <table>
            <tr>
              <td>序号</td>
              <td>姓名</td>
              <td>年龄</td>
              <td>性别</td>
            </tr>
            <%
              List<Student> list = (List<Student>) request.getAttribute("list");
                for (int i = 0; i < list.size(); i++) {%>
                  <tr>
                    <td><%=i + 1%>
                    </td>
                    <td><%=list.get(i).getName()%>
                    </td>
                    <td><%=list.get(i).getAge()%>
                    </td>
                    <td><%=list.get(i).getGender()%>
                    </td>
                  </tr>
                  <%}%>
          </table>
        </body>
      </html>
```

6. 编写编码过滤器，解决中文乱码问题

> 由于没有设置请求与响应的编码格式，会导致传递的数据出现乱码，如下（数据插入数据库就是乱码）：
>
> ![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727503408960-5bb3cc10-bb54-42ce-8005-2789d0f71b26.png)
>
> 所以需要设置编码过滤器，将请求与响应过滤出来，进行编码格式设置，这一点通过 Servlet 设置
>

+ 编写过滤器

```java
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

+ 配置 web.xml，将编码过滤器配置进去

> 这里使用xml配置，而不是用注解@WebServlet("/*") 配置，注解配置失败，不知道为什么
>

```xml
<!--  配置编码过滤器-->
<filter>
  <filter-name>EncodingFilter</filter-name>
  <filter-class>com.slz.springmvc.filter.EncodingFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>EncodingFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

<h4 id="Xf66j">SpringMvc 返回 JSON 格式数据</h4>
前后端分离开发，后端只写接口，统一返回 json 格式数据，这也符合 **<font style="color:#E4495B;">RESTFul 编码风格</font>**

<h5 id="Zag5I">方法一 <font style="color:#E4495B;">@ResponseBody</font></h5>
1. pom.xml 引入 JSON 格式数据处理依赖

```xml
<!--    引入json格式依赖-->
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>jsr250-api</artifactId>
  <version>1.0</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-annotations</artifactId>
  <version>2.13.2</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.13.2</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.13.2</version>
</dependency>
```

2. 编写返回 JSON 格式的 Controller

> `<font style="color:#E4495B;">@ResponseBody</font>`<font style="color:#E4495B;"> 注解表明当前方法返回的是一个json数据</font>
>

```java
@Controller
public class JsonController {

    @GetMapping("returnJson")
    // 使用下面注解，表明当前方法返回的是一个json数据
    @ResponseBody
    public Object json(){
        Student student = new Student();
        student.setName("荀彧");
        student.setAge(25);
        student.setGender("男");
        return student;
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727504861408-cea5b142-d198-4b99-9514-9f23c3c23d84.png)

<h5 id="IteO7">方法二 <font style="color:#E4495B;">@RestControler</font></h5>
如果要求当前 Controller 中所有方法都以 json 格式返回，使用 **<font style="color:#E4495B;">@RestController</font>** 标记该Controller 类

```java
// 如果当前 controller 中所有方法都 json 返回，使 @RestController 替代 @Controller 
@RestController
public class JsonController {

    @GetMapping("returnJson")
    public Object json(){
        Student student = new Student();
        student.setName("荀彧");
        student.setAge(25);
        student.setGender("男");
        return student;
    }
}
```

![](https://cdn.nlark.com/yuque/0/2024/png/42892034/1727505194392-36620301-ee91-4eda-814b-f0d849eb43f7.png)











