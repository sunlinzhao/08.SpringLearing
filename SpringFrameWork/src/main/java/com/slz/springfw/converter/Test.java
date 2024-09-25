package com.slz.springfw.converter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("converterBean.xml");
        Baby baby = context.getBean("baby", Baby.class);
        System.out.println(baby);
    }
}
