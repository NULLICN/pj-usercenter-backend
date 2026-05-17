package xyz.nullicn.projectstudyusercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.nullicn.projectstudyusercenter.mapper")
public class ProjectstudyUsercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectstudyUsercenterApplication.class, args);
    }

}
