package com.example.usermanageback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;


@SpringBootApplication
@MapperScan("com.example.usermanageback.mapper")
public class UserManageBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManageBackApplication.class, args);
    }

}
