package com.jakie.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jakie.usercenter.constrant.UserConstant;
import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.model.domain.request.UserLoginRequest;
import com.jakie.usercenter.model.domain.request.UserRegisterRequest;
import com.jakie.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户登录、注册
 **/
@RestController
@RequestMapping(("/user"))
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();

        String userPassword = userLoginRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }


    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();


        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
    }

    @PostMapping("/logout")
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.LOGIN_USER_ACCOUNT);
        return 1;
    }






    @GetMapping("/search")
    public List<User> search(@RequestParam("username") String username, HttpServletRequest request) {
        //鉴权 仅管理员可搜索
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }


        if (!StringUtils.isNotBlank(username)) {
            return new ArrayList<>();
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username", username);
        List<User> list = userService.list(userQueryWrapper);
        return list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }


    @PostMapping("/delete")
    public boolean delete(@RequestBody long id, HttpServletRequest request) {
        System.out.println(id);
        //鉴权 仅管理员可删除
        if (!isAdmin(request)) {
            return false;
        }

        if (id <= 0) {
            return false;
        }

        return userService.removeById(id);
    }

    /**
     * @Description: 抽取公共逻辑 delete和search方法中都用到了该方法
     **/
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.LOGIN_USER_ACCOUNT);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_UESR;
    }




}
