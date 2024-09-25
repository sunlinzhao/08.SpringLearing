package com.slz.mybatis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */

@Data
@Accessors(chain = true)
public class Student {
    private int id;
    private String name;
    private int age;
    private String gender;
}
