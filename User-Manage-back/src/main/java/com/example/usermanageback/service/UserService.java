package com.example.usermanageback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanageback.pojo.DTO.LoginDTO;
import com.example.usermanageback.pojo.DTO.RegisterDTO;
import com.example.usermanageback.pojo.DTO.SearchDTO;
import com.example.usermanageback.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usermanageback.pojo.VO.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 15229
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-06-29 14:34:28
*/

public interface UserService extends IService<User> {
   long userRegister(RegisterDTO registerDTO);
   UserVO userLogin(LoginDTO loginDTO, HttpServletRequest request);
   List<UserVO> getUserList(SearchDTO searchDTO);

    int userLogout(HttpServletRequest request);

    UserVO getCurrentUser(HttpServletRequest request);
}
