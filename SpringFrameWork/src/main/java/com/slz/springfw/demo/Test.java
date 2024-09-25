package com.slz.springfw.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Test {
    public static void main(String[] args) {
        // 加载 Spring 容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        // 获取实例
//        Person person1 = (Person) ctx.getBean("Person");
//        Person person2 = (Person) ctx.getBean("Person");
//        // 打印实例
//        System.out.println(person1==person2);
//        Teacher teacher = ctx.getBean("Teacher", Teacher.class);
//        System.out.println(teacher);
        Student student = ctx.getBean("StudentY", Student.class);
        System.out.println(student);
    }
}
