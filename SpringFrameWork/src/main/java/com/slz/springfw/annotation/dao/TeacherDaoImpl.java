package com.slz.springfw.annotation.dao;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/22
 */
public class TeacherDaoImpl implements TeacherDao{
    @Override
    public void teach() {
        System.out.println("I AM YOUR TEACHER");
    }
}
