package com.daylyweb.music.interceptor;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.daylyweb.music.mapper.Admin;
import com.daylyweb.music.service.AdminService;

public class LoginInterceptor implements HandlerInterceptor{

	private final int STATUS_CODE_ERROR=500;
	
	@Autowired
	private AdminService as;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Admin login = (Admin) request.getSession().getAttribute("login");
		if(login!=null) {
			if(login.getPassword().equals(as.getByUserName(login.getUsername()).getPassword())){
				return true;
			}
		}
		response.setCharacterEncoding("UTF-8");
		Writer out = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("status", STATUS_CODE_ERROR);
		jo.put("msg", "请先登录！");
		out.write(jo.toString());
		return false;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
