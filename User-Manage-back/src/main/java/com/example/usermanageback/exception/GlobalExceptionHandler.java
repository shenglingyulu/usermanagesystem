package com.example.usermanageback.exception;

import com.example.usermanageback.common.BaseResponse;
import com.example.usermanageback.common.ErrorCode;
import com.example.usermanageback.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //对上传文件异常进行处理
    @ExceptionHandler(value = MultipartException.class)
    public BaseResponse<?> handleBusinessException(MaxUploadSizeExceededException ex) {
        String msg;
        if (ex.getCause().getCause() instanceof FileUploadBase.FileSizeLimitExceededException) {
            log.error(ex.getMessage());
            msg = "上传文件过大[单文件大小不得超过10M]";
        } else if (ex.getCause().getCause() instanceof FileUploadBase.SizeLimitExceededException) {
            log.error(ex.getMessage());
            msg = "上传文件过大[总上传文件大小不得超过10M]";
        } else {
            msg = "上传文件失败";
        }

        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,msg);

    }
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
