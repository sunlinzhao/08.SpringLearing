package com.slz.springfw.proxy.dynamicProxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class MyInterceptor implements MethodInterceptor {

    private Object obj; // 被代理对象
    public MyInterceptor(Object obj){
        this.obj = obj;
    }
    /**
     * 拦截器方法，用于在方法调用前后执行额外的操作
     *
     * @param o            被拦截对象的实例
     * @param method       正在调用的方法对象
     * @param objects      方法调用时传递的参数数组
     * @param methodProxy  方法代理对象，用于调用目标方法
     * @return             返回方法调用的结果，如果抛出异常则为异常信息
     * @throws Throwable   在方法调用或代理过程中可能抛出的异常
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行前");
        Object oo = method.invoke(obj, objects);
        System.out.println("执行后");
        return oo;
    }
}
