package com.slz.aop.aspect;

import org.springframework.stereotype.Component;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */

// 被代理类作为组件交给Spring容器管理
@Component
public class Waiter{
    // 切点
    public void showMenu(){
        System.out.println("请点菜");
    }
    // 切点
    public void pay(){
        System.out.println("请付款");
    }
    // 切点
    public Boolean check(String code){
        System.out.println("请问您会员编号是多少");
        if("123456".equals(code))
            return true;
        return false;
    }
    // 切点
    public String cook(String name){
        System.out.println("开始制作" + name);
        return "done";
    }

    // 切点
    public Integer calculate(){
        try {
            return 10/0;
        } catch (ArithmeticException e){
            // 处理异常
//            e.printStackTrace();
            // 抛出异常
            throw new RuntimeException(e);
        }
//        return null;
    }
}
