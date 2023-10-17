package cn.little.prince.onlinejudgebackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 349807102
 */
@SpringBootApplication
@ComponentScan("cn.little.prince")
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.little.prince.onlinejudgebackendserviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OnlineJudgeBackendJudgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeBackendJudgeServiceApplication.class, args);
    }
}
