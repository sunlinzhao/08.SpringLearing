package com.slz.springfw.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class MyInvocationHandler implements InvocationHandler {
    // 持有被代理类
    private Object obj;
    public MyInvocationHandler(Object obj){
        this.obj = obj;
    }

    /**
     * 动态代理目标方法的执行
     * 该方法是在代理对象的方法被调用时执行的，用于在目标方法执行前后添加额外的功能
     *
     * @param proxy 代理对象，即生成的代理实例
     * @param method 当前准备执行的方法
     * @param args 方法执行时传递的实际参数
     * @return 目标方法执行的结果
     * @throws Throwable 如果目标方法运行时抛出了异常，则进行抛出
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 对被代理对象的方法进行判断
        if("save".equals(method.getName())){
            // 目标对象方法执行前添加的功能
            System.out.println("执行前");
            // 获取被代理对象
            Object o = method.invoke(obj, args);
            // 目标对象方法执行后添加的功能
            System.out.println("执行后");
            // 返回被代理对象
            return o;
        } else {
            return method.invoke(obj, args);
        }
    }
}
