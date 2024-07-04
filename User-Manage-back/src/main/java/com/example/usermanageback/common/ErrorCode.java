package com.example.usermanageback.common;

public enum ErrorCode {
    PARAMS_ERROR(40000,"参数错误",""),
    NULL_ERROR(40010,"数据为空",""),
    NO_LOGIN(40011,"未登录",""),
    NO_AUTH(40100,"无权限",""),
    OPEARTION_ERROR(45000,"操作失败",""),
    SYSTEM_ERROR(50000,"系统內部异常",""),
    ;
    private int code;
    private String message;
    private String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
