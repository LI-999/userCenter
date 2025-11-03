package com.jakie.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4088731996260713032L;

    private String userAccount;

    private String userPassword;
}
