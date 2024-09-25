package com.slz.springfw.demo;


import lombok.Data;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/21
 */

@Data
public class Person {
    public Person() {
        System.out.println("Person Constructor");
    }
    private String name;
    private String gender;
    private Integer age;
    private Car car;
}
