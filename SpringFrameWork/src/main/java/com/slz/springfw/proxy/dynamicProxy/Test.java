package com.slz.springfw.proxy.dynamicProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Test {
    public static void main(String[] args) {
//        UserService proxy = (UserService) ProxyUtil.getProxy(new UserServiceImpl(),
//                new MyInvocationHandler(new UserServiceImpl()));
//        proxy.save("job");
        UserService proxy = (UserService) ProxyUtil.getProxy(new UserServiceImpl2(),
                new MyInvocationHandler(new UserServiceImpl2()));
        proxy.save("job");
    }
}
