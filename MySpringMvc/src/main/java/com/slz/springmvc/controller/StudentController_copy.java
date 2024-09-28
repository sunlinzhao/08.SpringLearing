package com.slz.springmvc.controller;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/26
 */


import com.slz.springmvc.entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * 相当于 Servlet，不需要继承 HttpServlet
 */

@Controller
@RequestMapping("stu")
public class StudentController_copy {
    @RequestMapping("login")
    public String login() {
        return "login";
    }
//    public String loginSubmit(HttpServletRequest request){
//        String name = request.getParameter("name");
//        String password = request.getParameter("password");
//        return null;
//    }

    @RequestMapping("loginSubmit")
    public String loginSubmit(String name, String password) { // spring mvc 可以直接从前端获取参数
        if ("admin".equals(name) && "123456".equals(password))
            return "success"; // 配置了视图解析器，可以直接返回文件名，进行页面跳转
        return "fail";
    }
    @RequestMapping("loginSubmit2")
    public String loginSubmit(Users users) { // spring mvc 可以直接从前端获取参数
        if ("admin".equals(users.getName()) && "123456".equals(users.getPassword()))
            return "success"; // 配置了视图解析器，可以直接返回文件名，进行页面跳转
        return "fail";
    }
    @RequestMapping("select")
    public ModelAndView selectList() {
        List<String> list = Arrays.asList("AA", "BB", "CC");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test.jsp"); // 要跳转的地址
        modelAndView.addObject("list", list); // req.setAttribute("list")
        return modelAndView;
    }

    @RequestMapping(value = "view", method = RequestMethod.POST)
    public ModelAndView save() {
        return null;
    }
    @RequestMapping("delete/{id}")
    public String delete(@PathVariable("id") String sid){
        System.out.println(sid);
        return "get";
    }
}
