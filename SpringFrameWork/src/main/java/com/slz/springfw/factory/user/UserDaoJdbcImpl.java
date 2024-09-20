package com.slz.springfw.factory.user;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class UserDaoJdbcImpl implements UserDao{
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
