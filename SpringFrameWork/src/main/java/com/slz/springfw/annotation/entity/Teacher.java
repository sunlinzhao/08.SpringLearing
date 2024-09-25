package com.slz.springfw.annotation.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
//@Data
@Component // 声明组件
@ToString
public class Teacher {
    @Value("${Teacher.name}")
    private String name;
    @Value("${Teacher.age}")
    private Integer age;
    @Resource
    //    @Qualifier("student")
    private Student student;
//    @Autowired
//    public Teacher(Student student) {
//        this.student = student;
//    }
}
