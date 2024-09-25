package com.slz.springfw.demo;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class UserDaoMybatisImpl implements UserDao {
    public UserDaoMybatisImpl() {
        System.out.println("Mybatis Constructor");
    }

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
