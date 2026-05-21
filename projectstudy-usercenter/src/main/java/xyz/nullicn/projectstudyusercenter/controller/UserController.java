package xyz.nullicn.projectstudyusercenter.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.nullicn.projectstudyusercenter.model.domain.User;
import xyz.nullicn.projectstudyusercenter.model.domain.request.UserLoginRequest;
import xyz.nullicn.projectstudyusercenter.model.domain.request.UserRegisterRequest;
import xyz.nullicn.projectstudyusercenter.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static xyz.nullicn.projectstudyusercenter.constant.UserConstant.ADMIN_ROLE;
import static xyz.nullicn.projectstudyusercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null) return null;

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }

    @PostMapping("/dologin")
    public User userDologin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null) return null;

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StrUtil.hasBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userdoLogin(userAccount, userPassword, request);
        return user;
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {

        if(!isAdmin(request)) {
            return new ArrayList<>();
        };

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }

        return userService.list(queryWrapper);
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(!isAdmin(request)) return false;

        if (id < 0) return false;

        return userService.removeById(id);
    }

    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false; // 没有拿到用户登陆态 以及 用户不是管理员 直接退出
        }
        return true;
    }
}
