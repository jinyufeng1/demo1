package com.example.demo1.console;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Properties;

@SpringBootTest
class ConsoleApplicationTests {
	@Value("${slave.url}")
	private String slaveUrl;
	@Value("${slave.username1}")
	private String slaveUsername;
	@Value("${slave.password}")
	private String slavePassword;
	@Value("${slave.decrypt.key}")
	private String slaveDecryptKey;
	@Test
	void contextLoads() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(slaveUrl);
		dataSource.setUsername(slaveUsername);
		dataSource.setPassword(slavePassword);
		// 创建 Properties 对象来设置连接属性
		Properties properties = new Properties();
		properties.setProperty("config.decrypt", "true");
		properties.setProperty("config.decrypt.key", slaveDecryptKey);
		// 设置连接属性
		dataSource.setConnectProperties(properties);
		dataSource.setFilters("config");

		// 测试连接
		// 如果 dataSource.getConnection() 方法成功返回一个 Connection 对象，则表示连接成功；如果抛出 SQLException 异常，则表示连接失败。
		DruidPooledConnection connection = dataSource.getConnection();
		if (null != connection) {
			System.out.println("连接成功: " + connection);
			connection.close();
		}
	}

}
