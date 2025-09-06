package com.jakie.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jakie.usercenter.common.BaseResponse;
import com.jakie.usercenter.common.ErrorCode;
import com.jakie.usercenter.common.ResultUtils;
import com.jakie.usercenter.constrant.UserConstant;
import com.jakie.usercenter.exception.BusinessException;
import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.model.domain.request.UserLoginRequest;
import com.jakie.usercenter.model.domain.request.UserRegisterRequest;
import com.jakie.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_PARAM);
        }
        String userAccount = userLoginRequest.getUserAccount();

        String userPassword = userLoginRequest.getUserPassword();


        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);

        return ResultUtils.success(user);
    }


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_PARAM);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();


        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }

        long l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(l);
    }

    @PostMapping("/logout")
    public BaseResponse<String> userLogout(HttpServletRequest request) {
        if(request==null){
            throw new BusinessException(ErrorCode.NULL_PARAM,"request is null");
        }
        request.getSession().removeAttribute(UserConstant.LOGIN_USER_ACCOUNT);
        return ResultUtils.success("登出成功");
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> search(String username, HttpServletRequest request) {
        //鉴权 仅管理员可搜索
        if (!isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }


        if (!StringUtils.isNotBlank(username)) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username", username);
        List<User> list = userService.list(userQueryWrapper);
        List<User> collect = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody long id, HttpServletRequest request) {
        //鉴权 仅管理员可删除
        if (!isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
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
