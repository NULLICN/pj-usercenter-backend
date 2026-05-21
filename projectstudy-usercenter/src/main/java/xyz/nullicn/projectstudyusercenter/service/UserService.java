package xyz.nullicn.projectstudyusercenter.service;

import jakarta.servlet.http.HttpServletRequest;
import xyz.nullicn.projectstudyusercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-05-17 22:59:01
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userdoLogin(String userAccount, String userPassword, HttpServletRequest request);
}
