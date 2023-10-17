package cn.little.prince.onlinejudgebackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author 349807102
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class OnlineJudgeBackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeBackendGatewayApplication.class, args);
    }
}
