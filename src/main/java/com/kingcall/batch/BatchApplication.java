package com.kingcall.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * spring-boot-starter-batch 自动引入spring-boot-starter-jdbc,后者会尝试实例化datasource。
 * 在@SpringBootApplication注解上增加exclude = {DataSourceAutoConfiguration.class}，避免Spring Boot 自动配置Datasource至数据库连接。
 */
@SpringBootApplication()
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}

