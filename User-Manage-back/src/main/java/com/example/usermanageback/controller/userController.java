package com.example.usermanageback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanageback.common.BaseResponse;
import com.example.usermanageback.common.ResultUtils;
import com.example.usermanageback.common.ErrorCode;
import com.example.usermanageback.exception.BusinessException;
import com.example.usermanageback.pojo.DTO.LoginDTO;
import com.example.usermanageback.pojo.DTO.RegisterDTO;
import com.example.usermanageback.pojo.DTO.SearchDTO;
import com.example.usermanageback.pojo.DTO.UpdateDTO;
import com.example.usermanageback.pojo.User;
import com.example.usermanageback.pojo.VO.UserVO;
import com.example.usermanageback.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.usermanageback.constant.constant.ADMIN_ROLE;
import static com.example.usermanageback.constant.constant.LOGIN_STATUS;

@RestController
@RequestMapping("/user")
public class userController {
    @Resource
    UserService userService;
    @PostMapping("/register")
    public BaseResponse<Long> UserRegister(@RequestBody RegisterDTO registerDTO)
    {
        if (registerDTO ==null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不正确");
        }
        long l = userService.userRegister(registerDTO);
        return ResultUtils.success(l);
     }

     @PostMapping("/login")
    public BaseResponse<UserVO> UserLogin(@RequestBody LoginDTO loginDTO, HttpServletRequest request)
     {
         if (loginDTO==null)
         {
             throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
         }
         if (request==null)
         {
             throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
         }
         UserVO userVO = userService.userLogin(loginDTO,request);
         return ResultUtils.success(userVO);
     }
     @GetMapping("/search")
    public BaseResponse<List<UserVO>> GetUser(SearchDTO searchDTO, HttpServletRequest request)
     {

         if (request==null)
         {
             throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空");
         }
         boolean admin = isAdmin(request);
         if (!admin)
         {
             throw new BusinessException(ErrorCode.NO_AUTH,"无权限");
         }
         List<UserVO> userByUserName = userService.getUserList(searchDTO);
         return ResultUtils.success(userByUserName);
     }
     @PostMapping("/remove")
    public BaseResponse<Boolean> RemoveUser(@RequestBody Long id)
     {
         if (id==null)
         {
             throw new BusinessException(ErrorCode.NULL_ERROR,"参数为空");
         }
         boolean result = userService.removeById(id);
         if (!result)
         {
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除失败");
         }
         return ResultUtils.success(true);
     }
     @PostMapping("/update")
    public BaseResponse<Boolean> UpdateUser(@RequestBody UpdateDTO updateDTO, HttpServletRequest request)
     {
         if (request==null)
         {
             throw new BusinessException(ErrorCode.NULL_ERROR,"数据为空");
         }
         Long id = updateDTO.getId();
         boolean admin = isAdmin(request);
         UserVO currentUser = userService.getCurrentUser(request);
         Long id1 = currentUser.getId();
         if (!admin&&!id.equals(id1))
         {
             throw new BusinessException(ErrorCode.NO_AUTH,"无权限修改");
         }

         QueryWrapper<User> queryWrapper=new QueryWrapper<>();
         queryWrapper.eq("id",id);
         User user=new User();
         BeanUtils.copyProperties(updateDTO,user);
         boolean update = userService.update(user,queryWrapper);
          if (!update)
          {
              throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存失败");
          }
         return ResultUtils.success(true);
     }
    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(LOGIN_STATUS);
        UserVO currentUser = (UserVO) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return ResultUtils.success(userVO);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(LOGIN_STATUS);
        UserVO user = (UserVO) userObj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }


}
