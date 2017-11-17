package com.daylyweb.music.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.daylyweb.music.mapper.Admin;

/**   
 * @ClassName:  LogInterceptor   
 * @Description:日志记录aop拦截器
 * @author: 丶Tik 
 * @date:   2017年11月17日 下午1:36:17     
 *  
 */ 
@Component
@Aspect
public class LogInterceptor {

	static Logger logger = LogManager.getLogger(LogInterceptor.class);
	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	String loginadmin = ((Admin)(request.getSession().getAttribute("login"))).getNickname();
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.MusicService.getMusic(..))",returning="result")
	public void afterGetMusic(List result) {
		logger.info("管理员："+loginadmin+" 取出了"+result.size()+"条音乐列表...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.MusicService.insert(..))",returning="result")
	public void afterInsertMusic(int result) {
		logger.info("用户搜索歌曲给曲库增加了"+result+"条音乐...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.MusicService.setCommend(..))",returning="result")
	public void afterSetCommend(int result) {
		logger.info("管理员："+loginadmin+" 修改了"+result+"条音乐的推荐状态...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.MusicService.deleteByIds(..))",returning="result")
	public void afterDeleteByIds(int result) {
		logger.info("管理员："+loginadmin+" 从库里删除了"+result+"条音乐...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.getZan(..))",returning="result")
	public void afterGetZan(List result) {
		logger.info("管理员："+loginadmin+" 取出了"+result.size()+"条点赞记录...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.insertZan(..))",returning="result")
	public void afterInsertZan(int result) {
		logger.info("某位用户给你点了"+result+"个赞，请在后台看看是何方神圣...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.delZan(..))",returning="result")
	public void afterDelZan(int result) {
		logger.info("管理员："+loginadmin+" 从库里删除了"+result+"条点赞记录...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.getFeedBack(..))",returning="result")
	public void afterGetFeedBack(List result) {
		logger.info("管理员："+loginadmin+" 取出了"+result.size()+"条反馈记录...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.insertFeedBack(..))",returning="result")
	public void afterInsertFeedBack(int result) {
		logger.info("某位用户给你反馈了"+result+"条信息，请在后台管理查看...");
	}
	
	@AfterReturning(pointcut="execution(* com.daylyweb.music.service.LogService.delFeedBack(..))",returning="result")
	public void afterDelFeedBack(int result) {
		logger.info("管理员："+loginadmin+" 从库里删除了"+result+"条反馈记录...");
	}
}
