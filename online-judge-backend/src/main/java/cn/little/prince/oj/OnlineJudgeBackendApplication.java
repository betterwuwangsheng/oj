package cn.little.prince.oj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 349807102
 */
@SpringBootApplication
@MapperScan("cn.little.prince.oj.mapper")
public class OnlineJudgeBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeBackendApplication.class, args);
    }
}
