package com.slz.springfw.factory.user;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class UserDaoMybatisImpl implements UserDao{
    @Override
    public void save() {
        System.out.println("【Mybatis】 save");
    }

    @Override
    public void select() {
        System.out.println("【Mybatis】 select");
    }

    @Override
    public void del() {
        System.out.println("【Mybatis】 del");
    }
}
