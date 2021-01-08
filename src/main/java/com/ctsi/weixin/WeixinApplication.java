package com.ctsi.weixin;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages={"com.ctsi.*"})
@MapperScan(basePackages = "com.ctsi", annotationClass = Mapper.class)
@EnableScheduling
@Configuration
public class WeixinApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinApplication.class, args);
    }

}
