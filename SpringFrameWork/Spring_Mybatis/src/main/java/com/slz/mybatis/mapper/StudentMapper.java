package com.slz.mybatis.mapper;

import com.slz.mybatis.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */

// 虽然 mapper 是接口，用注解标注为组件后，Spring 容器回自动代理生成实现类
@Repository
public interface StudentMapper {
    void save(Student student);
    List<Student> selectList();
}
