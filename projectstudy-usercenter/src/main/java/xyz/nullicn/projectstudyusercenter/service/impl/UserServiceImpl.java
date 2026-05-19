package xyz.nullicn.projectstudyusercenter.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.nullicn.projectstudyusercenter.model.User;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    UserMapper userMapper;

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

        // 检查字符串中是否包含至少一个非字母数字的符号
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
        // 2:18:00
    }
}




