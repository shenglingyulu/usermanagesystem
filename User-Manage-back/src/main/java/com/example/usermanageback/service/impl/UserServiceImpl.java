package com.example.usermanageback.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usermanageback.common.ErrorCode;
import com.example.usermanageback.exception.BusinessException;
import com.example.usermanageback.pojo.DTO.LoginDTO;
import com.example.usermanageback.pojo.DTO.RegisterDTO;
import com.example.usermanageback.pojo.DTO.SearchDTO;
import com.example.usermanageback.pojo.User;
import com.example.usermanageback.pojo.VO.UserVO;
import com.example.usermanageback.service.UserService;
import com.example.usermanageback.mapper.UserMapper;

import com.example.usermanageback.utils.InviteCodeGeneratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.usermanageback.constant.constant.*;


/**
* @author 15229
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-06-29 14:34:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    UserMapper userMapper;
    @Override
    public long userRegister(RegisterDTO registerDTO) {
        String userAccount = registerDTO.getUserAccount();
        String userPassword = registerDTO.getUserPassword();
        String checkPassword = registerDTO.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
        {
            throw new BusinessException(ErrorCode.NULL_ERROR,"不能留空");
        }

        //账户的话不小于4位不高于16位
        if (userAccount.length()<4||userAccount.length()>16)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不小于4位不高于16位");
        }
        //密码就不小于8位不高于16位
        if (userPassword.length()<8||userPassword.length()>16)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不小于8位不高于16位");
        }
        //账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //账号不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if (count>=1)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }

        User user=new User();
        user.setUsername(userAccount);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        Boolean flag=true;
        String inviteCode = "a";
        while(flag)
        {
            //邀请码生成
            String s = InviteCodeGeneratorUtils.generateUniqueCode();
            //邀请码是否重复
            QueryWrapper<User> checkInviteCode=new QueryWrapper<>();
            checkInviteCode.eq("invitation_code",s);
            long invitationCount = this.count(checkInviteCode);
            if (invitationCount<1)
            {
                inviteCode=s;
                flag=false;
            }
        }
        user.setInvitationCode(inviteCode);
        if (inviteCode==null)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统內部异常,请稍后重试");
        }

        boolean request = this.save(user);
        if (!request)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"添加用户失败");
        }
        return user.getId();
    }

    @Override
    public UserVO userLogin(LoginDTO loginDTO, HttpServletRequest request) {
        String userAccount = loginDTO.getUserAccount();
        String userPassword = loginDTO.getUserPassword();
        //非空
        if(StringUtils.isAnyBlank(userAccount,userPassword))
        {
            throw new BusinessException(ErrorCode.NULL_ERROR,"数据为空");
        }

        //账户的话不小于4位不高于16位
        if (userAccount.length()<4||userAccount.length()>16)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        //密码就不小于8位不高于16位
        if (userPassword.length()<8||userPassword.length()>16)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        //账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        //校验密码是否输入正确，要和数据库中的密文密码去对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        //我们要记录用户的登录态(session),将其存到服务器上
        request.getSession().setAttribute(LOGIN_STATUS,userVO);
        //返回用户信息（脱敏）
        return userVO;
    }

    @Override
    public List<UserVO> getUserList(SearchDTO searchDTO) {
        Long id = searchDTO.getId();
        String userAccount = searchDTO.getUserAccount();
        String username = searchDTO.getUsername();
        int current = searchDTO.getCurrent();
        int pageSize = searchDTO.getPageSize();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username))
        {
            queryWrapper.like("username",username);
        }
        if (id!=null)
        {
            queryWrapper.eq("id",id);
        }
        if (StringUtils.isNotBlank(userAccount))
        {
            queryWrapper.like("user_account",userAccount);
        }
        Page<User> page=new Page<>(current,pageSize);
        int total = userMapper.selectList(queryWrapper).size();
        Page<User> userPage=userMapper.selectPage(page,queryWrapper);
        //List<User> users = userMapper.selectList(queryWrapper);

        List<UserVO> userVOList=new ArrayList<>();
        for(User user : userPage.getRecords())
        {
            System.out.println(user);
            UserVO userVO=new UserVO();
            BeanUtils.copyProperties(user,userVO);

            userVOList.add(userVO);
        }
         return userVOList;
//        List<User> users = userMapper.selectList(queryWrapper);
//        if (users==null)
//        {
//            throw new BusinessException(ErrorCode.NULL_ERROR,"暂无用户");
//        }
//        List<UserVO> userVOList=new ArrayList<>();
//        for(User user :users)
//        {
//            System.out.println(user);
//            UserVO userVO=new UserVO();
//            BeanUtils.copyProperties(user,userVO);
//            userVOList.add(userVO);
//        }
//        return userVOList;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        if (request==null)
        {
            throw new BusinessException(ErrorCode.NULL_ERROR,"数据为空");
        }
                request.getSession().removeAttribute(LOGIN_STATUS);
        return 1;
    }

    @Override
    public UserVO getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(LOGIN_STATUS);
        UserVO user = (UserVO) userObj;
        return user;
    }

}




