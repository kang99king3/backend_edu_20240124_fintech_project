package com.hk.fintech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Value;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

//	@Value
//    private String resourceHandler;
//
//    @Value("${resource.location}")
//    private String resourceLocation;
//	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// TODO Auto-generated method stub
//		WebMvcConfigurer.super.addResourceHandlers(registry);
//	}
//	
	@Autowired
	LoginInterceptor loginInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//추가할 인터셉터는 로그인 확인하는 인터셉터
		registry.addInterceptor(loginInterceptor)
		        .addPathPatterns("/banking/**","/user/**")
		        .excludePathPatterns("/user/signup",
		        		             "/user/login",
		        		             "/user/logout",
		        		             "/user/adduser"); 
		 
	}
	
	
}
