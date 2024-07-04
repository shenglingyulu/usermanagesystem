package com.example.usermanageback.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {

    private static final long serialVersionUID = 366595768940483092L;
    String userAccount;//账号
    String userPassword;//密码
    String checkPassword;//检查密码
}
