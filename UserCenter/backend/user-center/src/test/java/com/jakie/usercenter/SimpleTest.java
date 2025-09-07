package com.jakie.usercenter;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jakie.usercenter.mapper.UserMapper;
import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import static com.jakie.usercenter.service.UserService.SALT;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {

    @Autowired
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSave() {

        User user = new User();
        user.setUsername("jakie");
        user.setUserAccount("1234");
        user.setUserPassword("123456");
        user.setGender(0l);
        user.setAvatarUrl("https://lh3.googleusercontent.com/ogw/AF2bZyicbY1VReZ_D81NqfwvVx-4dxGO1dbHxEAPoJ5PLUaoSw=s64-c-mo");

        boolean save = userService.save(user);

        System.out.println(user.getId());
        System.out.println(save);

        System.out.println(userService);
    }


    @Test
    public void testDigest(){
        String str = DigestUtils.md5DigestAsHex((SALT+"123456").getBytes());
        System.out.println(str);


//        System.out.println(DigestUtils.md5DigestAsHex("jakie12345678".getBytes()));

    }

}
