package com.slz.springfw.factory.user;

/**
 * @author : SunLZ
 * @project : SpringLearing
 * @date : 2024/9/20
 */
public class UserServiceImpl implements UserService {

    @Override
    public void save() {
//        UserDao UserDao = new UserDaoJdbcImpl();
//        UserDao UserDao = new UserDaoMybatisImpl();
        UserDao userDao = BeanFactory.getBean("userDao");
        userDao.save();
    }
}
