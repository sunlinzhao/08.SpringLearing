package com.slz.springmvc.service;

import com.slz.springmvc.entity.Student;

import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/27
 */
public interface StudentService {
    void save(Student student);
    List<Student> selectList();
}
