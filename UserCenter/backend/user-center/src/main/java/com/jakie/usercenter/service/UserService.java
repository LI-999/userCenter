package com.jakie.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jakie.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author Just Lee
* @description 针对表【user】的数据库操作Service
* @createDate 2025-08-24 15:59:05
*/
public interface UserService extends IService<User> {

    String SALT = "jakie";


    long userRegister(String username,String password,String checkPassword,String planetCode);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User loginUser);


}
