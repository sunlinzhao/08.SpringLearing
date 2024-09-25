package com.slz.mybatis.test;

import com.slz.mybatis.entity.Student;
import com.slz.mybatis.service.StudentService;
import com.slz.mybatis.service.StudentServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */
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
