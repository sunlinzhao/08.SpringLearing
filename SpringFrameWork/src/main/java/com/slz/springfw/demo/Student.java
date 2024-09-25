package com.slz.springfw.demo;

import lombok.Data;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/21
 */
@Data
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

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + car +
                '}';
    }
}
