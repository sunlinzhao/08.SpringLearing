package com.slz.springmvc.service;

import com.slz.springmvc.entity.Student;
import com.slz.springmvc.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/27
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;
    @Override
    public void save(Student student) {
        studentMapper.save(student);
    }

    @Override
    public List<Student> selectList() {
        return studentMapper.selectList();
    }
}
