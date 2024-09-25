package com.slz.aop.aspect;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class TestAOP {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
//        Waiter bean = context.getBean(Waiter.class);
//        bean.showMenu();
//        String res = bean.cook("重庆小面");
//        System.out.println(res);
//        bean.check("123456");
//        bean.pay();
//        bean.calculate();
        UserDao bean = context.getBean(UserDao.class);
        bean.save();
    }
}
