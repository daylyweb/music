package com.daylyweb.music.utils;

import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
@Component
public class ConnectionFactory {
	public static DataSource dataSource;
	public static ApplicationContext context = new ClassPathXmlApplicationContext(  
            "classpath:spring.xml");
    public static DataSource getDataSource() throws SQLException {
    	dataSource = (DataSource) context.getBean("dataSource");
		return dataSource;
    }
}

