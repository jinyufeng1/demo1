package com.example.demo1.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class AppApplicationTests {

	@Autowired
	private DataSource dataSource;
	@Test
	void contextLoads() {
		System.out.println(dataSource.getClass());
	}

	@Test
	void test() {
//		DruidDataSource druidDataSource = (DruidDataSource) dataSource;
//		System.out.println("最大连接数：" + druidDataSource.getMaxActive());
//		System.out.println("初始化连接数：" + druidDataSource.getInitialSize());
	}

}
