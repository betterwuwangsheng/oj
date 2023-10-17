package cn.little.prince.oj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j 接口文档配置
 *
 * @author 349807102
 */
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    @Bean
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder().title("Online-Judge 在线评测系统接口文档").contact(new Contact("小王子", "https://github.com/betterwuwangsheng/", "little_prince@163.com")).description("Online-Judge 在线评测系统").version("1.0").build()).select()
                // 指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("cn.little.prince.oj.controller")).paths(PathSelectors.any()).build();
    }
}
