package com.slz.springfw.single;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/21
 */
public class Test {
    public static void main(String[] args) {
        SingleTon singleTon1 = SingleTon.getInstance();
        SingleTon singleTon2 = SingleTon.getInstance();
        System.out.println(singleTon1==singleTon2);
    }
}
