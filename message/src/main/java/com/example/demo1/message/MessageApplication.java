package com.example.demo1.message;

import com.aliyun.dysmsapi20170525.Client;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.demo1") // 指定spring bean的扫描路径
@MapperScan("com.example.demo1.*.mapper") // 指定mybatis bean的扫描路径
@EnableScheduling
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @Bean
    public Client client() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 私密信息不能上传到github
                .setAccessKeyId("")
                .setAccessKeySecret("")
                .setEndpoint("");
        return new Client(config);
    }
}
