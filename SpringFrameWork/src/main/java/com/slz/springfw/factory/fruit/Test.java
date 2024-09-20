package com.slz.springfw.factory.fruit;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Test {
    public static void main(String[] args) {
        Fruit fruit1 = new Factory().getFruit("apple");
        fruit1.plant();
        fruit1.grow();
        fruit1.harvest();
        Fruit fruit2 = new Factory().getFruit("orange");
        fruit2.plant();
        fruit2.grow();
        fruit2.harvest();
    }
}
