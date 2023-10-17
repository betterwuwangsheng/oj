package cn.little.prince.onlinejudgebackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author 349807102
 */
@SpringBootApplication
@MapperScan("cn.little.prince.onlinejudgebackendquestionservice.mapper")
@ComponentScan("cn.little.prince")
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.little.prince.onlinejudgebackendserviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OnlineJudgeBackendQuestionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeBackendQuestionServiceApplication.class, args);
    }
}
