package com.example.usermanageback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usermanageback.common.BaseResponse;
import com.example.usermanageback.common.ErrorCode;
import com.example.usermanageback.common.ResultUtils;
import com.example.usermanageback.exception.BusinessException;
import com.example.usermanageback.pojo.User;
import com.example.usermanageback.pojo.VO.UserVO;
import com.example.usermanageback.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class uploadController {
    @Value("${file.request}")
    private String prefix;
    @Resource
    UserService userService;
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"图片为空");
        }

        if (request==null)
        {
            throw new BusinessException(ErrorCode.NULL_ERROR,"数据为空");
        }
        UserVO userVO = userService.getCurrentUser(request);
        if (userVO==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
        }
        Long id = userVO.getId();
        String originalFilename = file.getOriginalFilename();//获取文件名称
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//生成文件名称前缀
        assert originalFilename != null;
        String fileNameSuffix = "." + originalFilename.split("\\.")[1];//生成文件名称后缀
        String fileName = fileNamePrefix + fileNameSuffix;//文件名
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\";//获取路径
        String path = pre+fileName;

        try {
            file.transferTo(new File(path));//保存文件
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",id);
            User user =new User();
            String url=prefix+fileName;
            user.setAvatarUrl(url);
            boolean update = userService.update(user, queryWrapper);
            if (!update)
            {
                throw new BusinessException(ErrorCode.OPEARTION_ERROR,"上传失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.success("上传成功");
    }

}
