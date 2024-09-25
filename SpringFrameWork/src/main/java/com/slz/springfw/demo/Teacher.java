package com.slz.springfw.demo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/21
 */
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
