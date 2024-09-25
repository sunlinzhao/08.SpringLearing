package com.slz.springfw.proxy.staticProxy;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
public class FactoryProxy implements Platform{
    private Factory factory;
    @Override
    public void product() {
        add_fun1();
        add_fun2();
        if (factory==null){
            factory=new Factory();
        }
        factory.product();
        add_fun3();
    }
    public void add_fun1(){
        System.out.println("添加功能1");
    }
    public void add_fun2(){
        System.out.println("添加功能2");
    }
    public void add_fun3(){
        System.out.println("添加功能3");
    }
}
