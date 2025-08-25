package com.jakie.usercenter;

import com.jakie.usercenter.model.domain.User;
import com.jakie.usercenter.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {

    @Autowired
    private UserService userService;

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
        System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
        String s = DigestUtils.md5DigestAsHex(("abc" + "123456").getBytes());

        System.out.println(s);
        String s1 = DigestUtils.md5DigestAsHex(("abc" + "123456").getBytes());


        String s2 = DigestUtils.md5DigestAsHex((UUID.randomUUID().toString() + "123456").getBytes());
        System.out.println(s2);
        System.out.println(s1);

    }


}
