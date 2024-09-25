package com.slz.springfw.proxy.dynamicProxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(userService.getClass().getClassLoader());
        enhancer.setSuperclass(userService.getClass());
        enhancer.setCallback(new MyInterceptor(userService));
        UserService o = (UserService) enhancer.create();
        o.save("job");
    }
}
