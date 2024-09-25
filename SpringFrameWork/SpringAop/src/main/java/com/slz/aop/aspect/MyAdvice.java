package com.slz.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */

// 让当前通知类作为Spring容器的组件
@Component
// 使用AspectJ实现AOP
@Aspect
public class MyAdvice {
    // 切点复用，共用切点提出，使用别名
    @Pointcut(value = "execution(* com.slz.aop.aspect.*.save(..))")
    public void PointCut1(){}

    @Before(value = "PointCut1()")
    public void doSave(){
        System.out.println("前置通知");
    }
    // 前置通知
    @Before(value = "execution(* com.slz.aop..*.showMenu(..))")
    public void doBefore(){
        System.out.println("欢迎光临");
        System.out.println("很高兴为您服务");
        System.out.println("-----------");
    }

    // 最终通知
    @After(value = "execution(* com.slz.aop..*.pay(..))")
    public void doAfter(){
        System.out.println("-----------");
        System.out.println("谢谢惠顾，下次再来");
    }

    // 后置通知
    @AfterReturning(value = "execution(* com.slz.aop..*.check(..))", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result){
        // 或取切点方法参数
        Object[] args = joinPoint.getArgs();
        // 判断切点方法返回值
        if((Boolean) result)
            for (Object arg : args) {
                System.out.println("编号：" + arg + "，是会员，享受优惠");
            }
        else {
            System.out.println("非会员");
        }
    }

    // 环绕通知
    @Around(value = "execution(* com.slz.aop..*.cook(..))")
    public String doAround(ProceedingJoinPoint point) {
        System.out.println("开启事务");
        String res = null;
        try {
            // 获取目标方法参数
            String arg = (String) point.getArgs()[0];
            if("重庆小面".equals(arg))
                // 获取目标方法执行返回值 ProceedingJoinPoint 接口继承自JoinPoint，用以调用目标方法
                res = (String) point.proceed();
            else System.out.println("做不了");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        System.out.println("结束事务");
        // 需要将目标方法返回值返回，以便于在主程序调用该方法时可以获取到返回值
        return res;
    }

    // 异常通知
    @AfterThrowing(value = "execution(* com.slz.aop..*.calculate(..))", throwing = "e")
    public void doAfterThrowing(Exception e){
        System.out.println("出现异常" + e.toString());
    }
}
