package com.slz.springfw.factory.fruit;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Orange implements Fruit{
    @Override
    public void plant() {
        System.out.println("橘子种植");
    }

    @Override
    public void grow() {
        System.out.println("橘子生长");
    }

    @Override
    public void harvest() {
        System.out.println("橘子收获");
    }
}
