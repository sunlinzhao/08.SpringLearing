package com.slz.mybatis.service;

import com.slz.mybatis.entity.Student;
import com.slz.mybatis.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */

@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper mapper;
    @Override
    public void save(Student student) {
        mapper.save(student);
    }

    @Override
    public List<Student> selectList() {
        return mapper.selectList();
    }
}
