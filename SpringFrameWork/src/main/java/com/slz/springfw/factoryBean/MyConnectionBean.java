package com.slz.springfw.factoryBean;

import com.mysql.cj.jdbc.Driver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */
@Getter
@Setter
public class MyConnectionBean implements FactoryBean<Connection> {
    private String driver;
    private String url;
    private String username;
    private String password;
    @Override
    public Connection getObject() throws Exception {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    /*
     * 通过 FactoryBean 提供创建的对象，是否是单例模式
     * */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
