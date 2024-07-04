package com.example.usermanageback.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 7889413490962134425L;
    String userAccount;
    String userPassword;
}
