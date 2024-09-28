package com.slz.springmvc.controller;

import com.slz.springmvc.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/28
 */

// 如果当前 controller 中所有方法都 json 返回，使 @RestController 替代 @Controller
@RestController
public class JsonController {

    @GetMapping("returnJson")
    public Object json(){
        Student student = new Student();
        student.setName("荀彧");
        student.setAge(25);
        student.setGender("男");
        return student;
    }
}
