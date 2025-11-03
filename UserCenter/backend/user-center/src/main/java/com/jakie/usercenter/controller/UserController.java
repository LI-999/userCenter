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
//@CrossOrigin(value = "http://localhost:8000",allowCredentials = "true")
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
        return ResultUtils.success("logout success");
    }

    @GetMapping("/current")
    public BaseResponse<User> current(HttpServletRequest request){
        Object object = request.getSession().getAttribute(UserConstant.LOGIN_USER_ACCOUNT);
        User currentUser = (User) object;
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN,"the current user doesn't login");
        }
        // 对于用户修改频繁的系统来说最好是查询一次数据库
        // 如果不频繁 可以直接返回session域中已登录的用户信息
        long id = currentUser.getId();
        User user = userService.getById(id);

        if(user==null){
            throw new BusinessException(ErrorCode.NOT_EXISTS,"the current user doesn't exist");
        }

        if(user.getStatus()==UserConstant.FREEZE_USER){
            throw new BusinessException(ErrorCode.ACCOUNT_ERROR,"the current user has been freezed");
        }

        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> search(String username, HttpServletRequest request) {
        //鉴权 仅管理员可搜索
        if (!isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            userQueryWrapper.like("username", username);
        }
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
