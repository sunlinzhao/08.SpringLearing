package com.slz.springfw.annotation;

import com.slz.springfw.annotation.service.TeacherService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
public class Test2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContextA.xml");
        TeacherService service = context.getBean("service", TeacherService.class);
        service.teach();
    }
}
