package com.slz.springfw.factory.user;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class BeanFactory {
    public static <T> T getBean(String name){
        ResourceBundle bundle = ResourceBundle.getBundle("MyBean");
        String beanName = bundle.getString(name);
        try {
            Class<?> aClass = Class.forName(beanName);
            return (T) aClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
