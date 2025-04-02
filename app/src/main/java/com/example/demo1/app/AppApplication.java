package com.example.demo1.app;

import com.example.demo1.module.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@Slf4j
@SpringBootApplication(scanBasePackages = "com.example.demo1")
@MapperScan("com.example.demo1.*.mapper") //缩小范围，加速扫描，提高spring的启动效率
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	/**
	 * 先⽣成⼀个列表，⾥⾯有10000个1
	 */
	@PostConstruct
	public void init() {
		log.info("Bean 初始化完成，调用 init 方法");
		for (int i = 0; i < 10000; i++) {
			Constant.queue.add(1);
			Constant.queue2.add(1);
		}
		log.info("调用 init 方法完毕");
	}

}
