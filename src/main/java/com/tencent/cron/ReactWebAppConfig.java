package com.tencent.cron;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ReactWebAppConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry  registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        /*registry.addResourceHandler("/static/template/**").addResourceLocations("classpath:/template/");*/

    }
}
