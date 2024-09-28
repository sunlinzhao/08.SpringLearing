package com.slz.springmvc.mapper;

import com.slz.springmvc.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/27
 */
@Repository
public interface StudentMapper {
    void save(Student student);
    List<Student> selectList();
}
