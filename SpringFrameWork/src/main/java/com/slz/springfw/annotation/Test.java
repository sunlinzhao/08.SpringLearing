package com.slz.springfw.annotation;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        HikariDataSource dataSource = (HikariDataSource) context.getBean("dataSource");
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//            System.out.println(connection);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        Date date = (Date)context.getBean("date");
        System.out.println(date);
//        Teacher teacher = context.getBean(Teacher.class);
//        System.out.println(teacher);
    }
}
