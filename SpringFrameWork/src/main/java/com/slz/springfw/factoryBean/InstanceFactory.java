package com.slz.springfw.factoryBean;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/23
 */

@Setter
@Getter
public class InstanceFactory {
    private String driver;
    private String url;
    private String username;
    private String password;
    public Connection getInstance(){
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
