package com.hi.locf.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {
        "com.hi.locf.feature.locf.mapper",
        "com.hi.locf.feature.provision.mapper",
        "com.hi.locf.feature.closing.mapper"
})
public class MyBatisConfig {
}
