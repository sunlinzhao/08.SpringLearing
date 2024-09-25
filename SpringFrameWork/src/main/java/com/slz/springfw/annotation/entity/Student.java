package com.slz.springfw.annotation.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
@Data
@Component // 声明组件
public class Student {
    @Value("${Student.name}")
    private String name;
    @Value("${Student.age}")
    private Integer age;
}
