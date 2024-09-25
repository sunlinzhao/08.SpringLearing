package com.slz.springfw.proxy.dynamicProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class UserServiceImpl implements UserService{
    @Override
    public void save(String s) {
        System.out.println("保存了" + s);
    }
}
