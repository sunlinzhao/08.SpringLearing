package com.slz.springfw.factory.user;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.save();
    }
}
