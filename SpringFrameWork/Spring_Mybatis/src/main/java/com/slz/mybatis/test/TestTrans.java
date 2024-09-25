package com.slz.mybatis.test;

import com.slz.mybatis.service.AccService;
import com.slz.mybatis.service.AccServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/25
 */
public class TestTrans {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccService bean = context.getBean(AccService.class);
        bean.doPay(70);
    }
}
