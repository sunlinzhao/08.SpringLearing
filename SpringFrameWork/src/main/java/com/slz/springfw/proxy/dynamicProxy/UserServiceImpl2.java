package com.slz.springfw.proxy.dynamicProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class UserServiceImpl2 implements UserService{
    @Override
    public void save(String s) {
        System.out.println("2: 保存了" + s);
    }
}
