package com.tensquare.friend.config;

import com.tensquare.friend.Interceptor.JwtLnterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ApplicationConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtLnterceptor lnterceptor;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lnterceptor) //添加拦截器 JwtLnterceptor
                .addPathPatterns("/**") //拦截的路径
                .excludePathPatterns("/**/login"); //不拦截的路径
    }
}
