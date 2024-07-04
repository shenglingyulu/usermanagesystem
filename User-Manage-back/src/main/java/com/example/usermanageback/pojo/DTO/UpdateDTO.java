package com.example.usermanageback.pojo.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class UpdateDTO {
    private Long id;
    private String username;
    private String avatarUrl;
    private Integer gender;
    private String phone;
    private String email;
    private Integer userStatus;
    private Date createTime;
    private Date updateTime;
    private Integer role;

}
