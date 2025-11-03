package com.jakie.usercenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jakie.usercenter.common.ErrorCode;
import com.jakie.usercenter.constrant.UserConstant;
import com.jakie.usercenter.exception.BusinessException;
import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.service.UserService;
import com.jakie.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Just Lee
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-08-24 15:59:05
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //验证 是否为空或null
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.NULL_PARAM,"请求参数为空");
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账户长度过短");

        }

        if (userPassword.length() < 8 && checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"密码过短");
        }

        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"两次密码不正确");

        }

        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"星球参数过长");

        }

        //用户名不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"用户名包含特殊字符");

        }

        /**
         * 星球编号是否存在
         */
        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        userQueryWrapper1.eq("planetCode", planetCode);
        User user1 = userMapper.selectOne(userQueryWrapper1);
        if (user1 != null) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS,"星球编号已存在");
        }

        /**
         * 用户是否存在
         */
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", userAccount);

        User searchUser = userMapper.selectOne(userQueryWrapper);
        if (searchUser != null) {
            //该用户已经存在
            throw new BusinessException(ErrorCode.ALREADY_EXISTS,"用户已存在");
        }

        //对密码进行加密
        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodePassword);
        user.setPlanetCode(planetCode);
        int save = userMapper.insert(user);

        if (save != 1) {
            throw new BusinessException(ErrorCode.INSERT_ERROR,"数据库添加失败");
        }

        return user.getId();
    }

    /**
     * 用户登录
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.验证 是否为空或null
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"参数为空");
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账户长度过短");
        }

        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"密码长度过短");
        }

        //用户名不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"用户名包含特殊字符");
        }

        //2.查询用户账户密码是否正确

        String encodePassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//        System.out.println(encodePassword);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encodePassword);

        User loginUser = userMapper.selectOne(userQueryWrapper);
        if (loginUser == null) {
            log.info("the user login failed");
            throw new BusinessException(ErrorCode.NOT_EXISTS,"用户不存在");
        }

        //3.脱敏 （防止数据库表中字段 被泄露）
        User safetyUser = getSafetyUser(loginUser);

        //4.设置登录态
        request.getSession().setAttribute(UserConstant.LOGIN_USER_ACCOUNT, safetyUser);

        return safetyUser;
    }

    /**
     * 数据脱敏
     */
    @Override
    public User getSafetyUser(User loginUser) {
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_EXISTS,"the current user doesn't exist");
        }
        User safetyUser = new User();
        safetyUser.setId(loginUser.getId());
        safetyUser.setUsername(loginUser.getUsername());
        safetyUser.setUserAccount(loginUser.getUserAccount());
        safetyUser.setGender(loginUser.getGender());
        safetyUser.setAvatarUrl(loginUser.getAvatarUrl());
        safetyUser.setPhone(loginUser.getPhone());
        safetyUser.setStatus(loginUser.getStatus());
        safetyUser.setEmail(loginUser.getEmail());
        safetyUser.setCreateTime(loginUser.getCreateTime());
        safetyUser.setUserRole(loginUser.getUserRole());
        safetyUser.setPlanetCode(loginUser.getPlanetCode());
        return safetyUser;
    }


}




