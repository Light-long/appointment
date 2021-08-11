package com.terminus.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.terminus.user.mapper")
public class UserConfig {
}
