package com.slz.aop.aspect;

import org.springframework.stereotype.Component;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/24
 */

@Component
public class UserDaoImpl implements UserDao{
    @Override
    public void save() {
        System.out.println("save");
    }
}
