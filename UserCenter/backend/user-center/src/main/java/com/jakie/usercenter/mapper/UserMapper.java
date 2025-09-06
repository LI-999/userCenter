package com.jakie.usercenter.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jakie.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Just Lee
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-08-24 15:59:05
* @Entity com.jakie.usercenter.model.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    
}




