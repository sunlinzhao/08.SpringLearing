package com.slz.springfw.factory.fruit;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class Factory {
    public Fruit getFruit(String name){
        if("apple".equals(name)){
            return new Apple();
        } else if("orange".equals(name)){
            return new Orange();
        } else return null;
    }
}
