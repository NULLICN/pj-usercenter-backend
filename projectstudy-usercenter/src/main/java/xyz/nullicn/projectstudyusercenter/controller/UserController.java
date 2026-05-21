package xyz.nullicn.projectstudyusercenter.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nullicn.projectstudyusercenter.model.domain.User;
import xyz.nullicn.projectstudyusercenter.model.domain.request.UserLoginRequest;
import xyz.nullicn.projectstudyusercenter.model.domain.request.UserRegisterRequest;
import xyz.nullicn.projectstudyusercenter.service.UserService;

/**
 * 用户接口
 */
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
}
