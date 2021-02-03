package com.tencent;

import com.tencent.service.ApiService;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.tencent.*"})
@MapperScan(basePackages = "com.tencent.*", annotationClass = Mapper.class)
@EnableScheduling
@Configuration
public class WeixinApplication  {
    public static void main(String[] args) {
        SpringApplication.run(WeixinApplication.class, args);
    }

}
