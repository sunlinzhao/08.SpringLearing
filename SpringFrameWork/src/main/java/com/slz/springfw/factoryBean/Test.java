package com.slz.springfw.factoryBean;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("complexBean.xml");
        // 如果给定的id前面没有&符号，代表获取 FactoryBean 中 getObject() 返回的对象
//        Connection connection1 = (Connection) context.getBean("connection");
        // 如果给定的 id 前面有 & 符号，代表获取 FactoryBean 实现类的对象
//        MyConnectionBean connection2 = (MyConnectionBean) context.getBean("&connection");
        Connection connection3 = (Connection) context.getBean("staticConnection");
//        System.out.println(connection1);
//        System.out.println(connection2);
        System.out.println(connection3);
    }
}
