package xyz.nullicn.projectstudyusercenter;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nullicn.projectstudyusercenter.mapper.UserMapper;
import xyz.nullicn.projectstudyusercenter.model.User;

import java.util.List;

@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        // 遍历userList，打印每个用户对象到控制台
        userList.forEach(System.out::println);
    }

}
