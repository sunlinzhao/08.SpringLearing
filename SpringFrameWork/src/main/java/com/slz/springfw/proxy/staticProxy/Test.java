package com.slz.springfw.proxy.staticProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Test {
    public static void main(String[] args) {
        Platform platform = new FactoryProxy();
        platform.product();
    }
}
