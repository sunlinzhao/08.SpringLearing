package com.slz.springfw.demo;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class UserDaoJdbcImpl implements UserDao {
    public UserDaoJdbcImpl() {
        System.out.println("JDBC Constructor");
    }

    @Override
    public void save() {
        System.out.println("【JDBC】 save");
    }

    @Override
    public void select() {
        System.out.println("【JDBC】 select");
    }

    @Override
    public void del() {
        System.out.println("【JDBC】 del");
    }
}
