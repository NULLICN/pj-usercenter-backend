package xyz.nullicn.projectstudyusercenter;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nullicn.projectstudyusercenter.utils.PasswordUtil;

@SpringBootTest
class ProjectstudyUsercenterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void validateStr() {
        boolean containsSpecial = ReUtil.contains("[^a-zA-Z0-9]", "ABC123");
        System.out.println(containsSpecial);
    }

    @Test
    void salt() {
//        System.out.println(BCrypt.gensalt(4));
        // $2a$04$forFnOe.SqkGNcKMftPZ6O
        String hashedPassword = PasswordUtil.hashPassword("12345678");
        System.out.println(hashedPassword);
        boolean check = PasswordUtil.checkPassword("12345678", hashedPassword);
        System.out.println(check);
    }

}
