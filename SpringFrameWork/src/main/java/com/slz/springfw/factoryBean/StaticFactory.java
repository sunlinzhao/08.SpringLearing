package com.slz.springfw.factoryBean;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */

public class StaticFactory {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/Mybatis?useSSL=false&serverTimezone=GMT%2B8";
    private static final String username = "root";
    private static final String password = "root";
    public static Connection getInstance(){

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
