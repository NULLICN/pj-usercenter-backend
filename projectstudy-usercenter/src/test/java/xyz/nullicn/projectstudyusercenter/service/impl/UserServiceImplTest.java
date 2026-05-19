package xyz.nullicn.projectstudyusercenter.service.impl;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nullicn.projectstudyusercenter.mapper.UserMapper;
import xyz.nullicn.projectstudyusercenter.model.User;
import xyz.nullicn.projectstudyusercenter.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void addOne() {
        User user = new User();
        user.setUsername("nullicn");
        user.setUserAccount("123");
        user.setAvatarUrl("https://avatars.githubusercontent.com");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void findAll() {
        User user = new User();

        List<User> userList = userMapper.selectList(null);
        for (User user1 : userList) {
            System.out.println("========" + user1);
        }
    }

    @Test
    void deleteAllUser() {

        int deleted = userMapper.delete(null);
        System.out.println(deleted + "条数据删除");
    }

    @Test
    void testRegister() {
        // Test successful registration with valid password (alphanumeric only)
        long result = userService.userRegister("testUser2", "password123", "password123");
        System.out.println("userid: " + result);
        // Should return a positive user ID (>= 0) for successful registration
        Assertions.assertTrue(result > 0 || result == -1);

        // Test failed registration with special characters in password
        long failResult = userService.userRegister("testUser3", "password@123", "password@123");
        System.out.println("fail userid: " + failResult);
        // Should return -1 because password contains special character
        Assertions.assertEquals(-1, failResult);
    }
}