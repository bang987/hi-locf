package com.hi.locf.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.hi.locf.feature.locf.mapper")
public class MyBatisConfig {
}
