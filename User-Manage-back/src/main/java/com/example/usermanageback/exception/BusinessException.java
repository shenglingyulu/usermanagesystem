package com.example.usermanageback.exception;

import com.example.usermanageback.common.ErrorCode;

public class BusinessException extends RuntimeException{
    private int code;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    private String description;
   public BusinessException(String message,int code,String description)
   {
       super(message);
       this.code=code;
       this.description=description;
   }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description=errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description=description;
    }


}
