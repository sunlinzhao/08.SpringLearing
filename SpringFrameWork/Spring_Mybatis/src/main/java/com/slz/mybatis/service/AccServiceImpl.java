package com.slz.mybatis.service;

import com.slz.mybatis.entity.Acc;
import com.slz.mybatis.entity.Records;
import com.slz.mybatis.mapper.AccMapper;
import com.slz.mybatis.mapper.RecordsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/25
 */
@Service
public class AccServiceImpl implements AccService {
    @Resource
    private AccMapper accMapper;
    @Resource
    private RecordsMapper recordsMapper;

//    @Transactional(
//            propagation = Propagation.REQUIRED, // 配置传播机制
//            isolation = Isolation.REPEATABLE_READ, // 配置事务隔离机制
//            timeout = 10,
//            rollbackFor = { // 配置回滚异常
//                    FileNotFoundException.class // 加入回滚后，即使检查型异常也会回滚
//            },
//            noRollbackFor = { // 配置不回滚异常
//                    NullPointerException.class
//            }
//    )
    @Override
    public void doPay(int money) {
        // 存一条交易记录
        Records records = new Records().setAccno("123456").setCategory("支出").setMoney(money);
        recordsMapper.insert(records);
        System.out.println("执行交易记录处理");
        //-------------------------- 异常回滚情况-------------------

        // 一、运行时异常
        try {
            int x = 10 / 0;
        } catch (ArithmeticException e){
//            e.printStackTrace(); // 1. 如果异常被处理，则不会回滚
            throw new RuntimeException(e); // 2. 如果抛出异常，则会回滚
        }
//        int x = 10 / 0; // 3. 程序异常而中止，会回滚
        // 二、检查时异常
//        throw new FileNotFoundException("文件找不到"); // 检查型异常，不会回滚

        //----------------------------------------------------------
        // 修改用户原账户下的余额
        Acc acc = new Acc().setAccno("123456").setMoney(money);
        accMapper.update(acc);
        System.out.println("执行用余额修改处理");
    }
}
