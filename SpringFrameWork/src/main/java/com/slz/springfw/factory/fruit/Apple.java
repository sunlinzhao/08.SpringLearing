package com.slz.springfw.factory.fruit;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Apple implements Fruit{
    @Override
    public void plant() {
        System.out.println("苹果种植");
    }

    @Override
    public void grow() {
        System.out.println("苹果生长");
    }

    @Override
    public void harvest() {
        System.out.println("苹果收获");
    }
}
