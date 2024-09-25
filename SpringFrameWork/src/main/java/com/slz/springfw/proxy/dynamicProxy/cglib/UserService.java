package com.slz.springfw.proxy.dynamicProxy.cglib;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class UserService {
    public void save(String s){
        System.out.println("保存了" + s);
    }
}
