package com.jakie.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.jakie.usercenter.mapper")
public class UserCenterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(UserCenterApplication.class, args);
//        for (String beanDefinitionName : run.getBeanDefinitionNames()) {
//            System.out.println(beanDefinitionName);
//        }
    }

}
