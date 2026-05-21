package xyz.nullicn.projectstudyusercenter.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.nullicn.projectstudyusercenter.model.domain.User;
import xyz.nullicn.projectstudyusercenter.service.UserService;
import xyz.nullicn.projectstudyusercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;
import xyz.nullicn.projectstudyusercenter.utils.PasswordUtil;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-05-17 22:59:01
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    UserMapper userMapper;

    public static final String USER_LOGIN_STATE = "userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验
        if(StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if(userAccount.length() < 4) {
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 仅通行纯字母数字密码，否则错误
        boolean containsSpecial = ReUtil.contains("[^a-zA-Z0-9]", userPassword);
        if(containsSpecial) return -1;

        // 账户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            return -1;
        }

        // 加密
        String encryptedPassword = PasswordUtil.hashPassword(userPassword);

        // 添加用户
        User newUser = new User();
        newUser.setUserPassword(encryptedPassword);
        newUser.setUserAccount(userAccount);
        boolean saveResult = this.save(newUser);
        if (!saveResult) {
            return -1;
        }

        return newUser.getId();

    }

    @Override
    public User userdoLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if(StrUtil.hasBlank(userAccount, userPassword)) {
            return null;
        }
        if(userAccount.length() < 4) {
            return null;
        }
        if(userPassword.length() < 8) {
            return null;
        }

        // 仅通行纯字母数字密码，否则错误
        boolean containsSpecial = ReUtil.contains("[^a-zA-Z0-9]", userPassword);
        if(containsSpecial) return null;

        // 加密
        String encryptedPassword = PasswordUtil.hashPassword(userPassword);

        // 账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptedPassword);
        User user = userMapper.selectOne(queryWrapper);
        log.info(user.getUserPassword() + " ?== " + encryptedPassword);
        if(user == null) {
            return null;
        }

        // 用户脱敏
        User safetyUser = new User();
        // 排除所有不需要的字段（剩下9个字段被拷贝）
        cn.hutool.core.bean.BeanUtil.copyProperties(user, safetyUser,
                "userPassword", "isDelete", "updateTime", "userRole", "planetCode");

        // 用户登陆态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }
}
// 下 30：00



