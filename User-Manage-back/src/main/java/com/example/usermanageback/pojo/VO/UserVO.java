package com.example.usermanageback.pojo.VO;

import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private Long id;
    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除0:否 1:s
     */
    private Integer isDelete;

    /**
     * 0 : 普通用户 1：管理员
     */
    private Integer role;

    /**
     * 邀请码
     */
    private String invitationCode;


}
