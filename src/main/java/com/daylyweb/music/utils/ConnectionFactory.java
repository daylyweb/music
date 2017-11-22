package com.daylyweb.music.utils;

import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**   
 * @ClassName:  ConnectionFactory   
 * @Description: log4j2的数据源工厂
 * @author: 丶Tik 
 * @date:   2017年11月22日 下午1:33:41     
 *  
 */ 
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

