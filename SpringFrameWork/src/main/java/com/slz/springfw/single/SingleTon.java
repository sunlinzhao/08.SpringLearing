package com.slz.springfw.single;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/21
 */

// 饿汉式
//public class SingleTon {
//    // 1. 私有化构造方法
//    private SingleTon() {
//    }
//
//    // 2. 私有化静态属性
//    private static SingleTon singleTon = new SingleTon();
//
//    public static SingleTon getInstance() {
//        return singleTon;
//    }
//}

// 懒汉式
public class SingleTon {
    // 1. 私有化构造方法
    private SingleTon() {
    }
    // 2. 私有化静态属性
    private static SingleTon singleTon = null;
    // 公有静态方法 getInstance
    public static SingleTon getInstance() {
        if(singleTon==null){
            singleTon = new SingleTon();
        }
        return singleTon;
    }
}