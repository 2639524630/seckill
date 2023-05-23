package com.you.seckill.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.you.seckill.mapper")
@Configuration
public class MybatisConfig {
}
