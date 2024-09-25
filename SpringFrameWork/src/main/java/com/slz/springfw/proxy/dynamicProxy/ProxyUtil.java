package com.slz.springfw.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class ProxyUtil {
    // 获取代理对象实例
    public static Object getProxy(Object target, InvocationHandler handler) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
    }
}
