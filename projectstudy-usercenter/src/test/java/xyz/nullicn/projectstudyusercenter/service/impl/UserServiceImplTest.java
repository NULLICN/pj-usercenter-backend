package xyz.nullicn.projectstudyusercenter.service.impl;
import java.util.Date;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nullicn.projectstudyusercenter.model.User;
import xyz.nullicn.projectstudyusercenter.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Test
    void findAll() {
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
}