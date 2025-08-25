package com.jakie.usercenter.service.impl;

import com.jakie.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void userRegister() {
        //userAccount 小于4
        String userAccount = "ja";
        String userPassword="123456";
        long l = userService.userRegister(userAccount, userPassword, userPassword);
        Assertions.assertEquals(-1,l);

        //密码 小于 6
        userAccount = "jakie";
        userPassword = "123";
        l = userService.userRegister(userAccount, userPassword, userPassword);
        Assertions.assertEquals(-1,l);

        //两次密码不一样
        userPassword="123456";
        String checkPassword = "1234567";
        l = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,l);


        //用户已存在
        userAccount = "1234";
        userPassword = "123456";
        l = userService.userRegister(userAccount, userPassword, userPassword);
        Assertions.assertEquals(-1,l);

        //正常情况
        userAccount="Curry";
        userPassword = "123456";
        l = userService.userRegister(userAccount, userPassword, userPassword);
        Assertions.assertTrue(l > 0);
    }
}