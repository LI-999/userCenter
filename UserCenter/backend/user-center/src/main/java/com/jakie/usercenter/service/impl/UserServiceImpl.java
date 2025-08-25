package com.jakie.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.service.UserService;
import com.jakie.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
* @author Just Lee
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-08-24 15:59:05
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //验证 是否为空或null
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }

        if(userAccount.length()<4){
            return -1;
        }

        if(userPassword.length()<6){
            return -1;

        }

        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        //用户名不能包含特殊字符

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",userAccount);

        long countUser = this.count(userQueryWrapper);
        if(countUser>0){
            //该用户已经存在
            return -1;
        }

        //对密码进行加密
        final String SALT = UUID.randomUUID().toString();
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUsername(userAccount);
        user.setUserPassword(encodePassword);
        boolean save = this.save(user);

        if(!save){
            return -1;
        }

        return user.getId();
    }


//    QueryWrapper
}




