package xyz.nullicn.projectstudyusercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.nullicn.projectstudyusercenter.model.User;
import xyz.nullicn.projectstudyusercenter.service.UserService;
import xyz.nullicn.projectstudyusercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-05-17 22:59:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




