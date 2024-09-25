package com.slz.springfw.proxy.staticProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class Factory implements Platform {
    @Override
    public void product() {
        System.out.println("工厂生产产品");
    }
}
