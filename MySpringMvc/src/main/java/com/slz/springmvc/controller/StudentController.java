package com.slz.springmvc.controller;

import com.slz.springmvc.entity.Student;
import com.slz.springmvc.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/27
 */

@Controller
@RequestMapping("student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("select")
    public String select(HttpServletRequest request){
        List<Student> students = studentService.selectList();
        request.setAttribute("list", students);
        return "list";
    }

    @PostMapping("insert")
    public String save(Student student){
        studentService.save(student);
        // 重定向
        return "redirect:select";
    }
}
