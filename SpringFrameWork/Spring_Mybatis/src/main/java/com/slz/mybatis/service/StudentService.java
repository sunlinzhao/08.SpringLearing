package com.slz.mybatis.service;

import com.slz.mybatis.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */
public interface StudentService {
    void save(Student student);
    List<Student> selectList();
}
