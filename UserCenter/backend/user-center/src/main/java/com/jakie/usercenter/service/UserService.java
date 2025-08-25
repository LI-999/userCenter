package com.jakie.usercenter.service;

import com.jakie.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Just Lee
* @description 针对表【user】的数据库操作Service
* @createDate 2025-08-24 15:59:05
*/
public interface UserService extends IService<User> {
    long userRegister(String username,String password,String checkPassword);

}
