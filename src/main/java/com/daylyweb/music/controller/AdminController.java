package com.daylyweb.music.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daylyweb.music.mapper.Admin;
import com.daylyweb.music.mapper.FeedBack;
import com.daylyweb.music.mapper.Zan;
import com.daylyweb.music.service.AdminService;
import com.daylyweb.music.service.LogService;
import com.daylyweb.music.service.MusicService;
import com.mysql.cj.api.Session;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final int STATUS_CODE_SUCCESS=200;
	private final int STATUS_CODE_ERROR=500;
	private final int STATUS_CODE_NOT_FOUND=404;
	
	@Autowired
	private AdminService as;
	@Autowired
	private LogService ls;
	@Autowired
	private MusicService ms;
	
	static Logger logger = LogManager.getLogger(AdminController.class);
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public ModelAndView admin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String viewname="admin/login";
		Admin login = (Admin) request.getSession().getAttribute("login");
		if (login!=null ) {
			if(login.getPassword().equals(as.getByUserName(login.getUsername()).getPassword())){
				viewname="admin/admin";
			}
		}
		mav.setViewName(viewname);
		return mav;
	}
	
	@RequestMapping(value="",method=RequestMethod.POST,params= {"username","password"})
	public ModelAndView adminLogin(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		String viewname = "admin/login";
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		Admin login = (Admin) session.getAttribute("login");
		login = login==null? as.getByUserName(username):login;
		if(login!=null){
			if(password.equals(login.getPassword())) {
				session.setAttribute("login", login);
				session.setAttribute("lasttime", login.getLasttime());
				session.setAttribute("lastip", login.getLastip());
				login.setLastip(request.getRemoteAddr());
				login.setLasttime(new java.util.Date());
				as.update(login);
				viewname="admin/admin";
			}
		}
		mav.setViewName(viewname);
		return mav;
	}
	
	@RequestMapping("/out")
	public String loginout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("login");
		session.removeAttribute("lasttime");
		session.removeAttribute("lastip");
		return "redirect:admin/admin";
	}
	
	@ResponseBody
	@RequestMapping(value="/api/user",method=RequestMethod.PUT)
	public Map modifyPass(String oldpass,String newpass1,String newpass2,HttpServletRequest request) {
		String msg="参数错误或原密码不正确！";
		int statuscode;
		boolean flag=false;
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(oldpass) && StringUtils.isNotEmpty(newpass1) && StringUtils.isNotEmpty(newpass2)) {
			Admin login = (Admin) request.getSession().getAttribute("login");
			if(oldpass.equals(login.getPassword()) && newpass1.equals(newpass2)) {
				login.setPassword(newpass1);
				flag = as.update(login);
			}
		}
		msg = flag ? "修改成功！请牢记新密码！":msg;
		statuscode = flag ? STATUS_CODE_SUCCESS:STATUS_CODE_ERROR;
		return this.setStatus(map, statuscode, msg);
	}
	
	@ResponseBody
	@RequestMapping(value="/api/zan",method=RequestMethod.GET)
	public Map getZan(String page,String limit,String keyword,String start,String end) {
		List list = null;
		int intpage=0,intlimit=0;
		long longstart=0,longend=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(limit)) { //page 和 limit必须同时传入 否则置0查询全部
			intpage = Integer.parseInt(page);
			intlimit = Integer.parseInt(limit);
		}
		keyword = StringUtils.isNotEmpty(keyword) ? keyword:null; //keyword非空为模糊查询
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			longstart = Long.parseLong(start);
			longend = Long.parseLong(end);
		}
		list = ls.getZan(intpage, intlimit, longstart, longend, keyword);
		if(list!=null && list.size()>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
			map.put("count",ms.getLastCount());
			map.put("data", list);
		} else {
			map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无记录！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/api/zan",method=RequestMethod.DELETE)
	public Map deleteZan(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(ids==null) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
		}else {
			result = ls.delZan(ids);
		}
		if(result>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "操作成功！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "操作失败！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/api/feedback",method=RequestMethod.GET)
	public Map getFeedBack(String page,String limit,String keyword,String start,String end) {
		List list = null;
		int intpage=0,intlimit=0;
		long longstart=0,longend=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(limit)) { //page 和 limit必须同时传入 否则置0查询全部
			intpage = Integer.parseInt(page);
			intlimit = Integer.parseInt(limit);
		}
		keyword = StringUtils.isNotEmpty(keyword) ? keyword:null; //keyword非空为模糊查询
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			longstart = Long.parseLong(start);
			longend = Long.parseLong(end);
		}
		list = ls.getFeedBack(intpage, intlimit, longstart, longend, keyword);
		
		if(list!=null && list.size()>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
			map.put("count",ms.getLastCount());
			map.put("data", list);
		} else {
			map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无记录");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/api/feedback",method=RequestMethod.DELETE)
	public Map deleteFeedBack(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(StringUtils.isEmpty(ids)) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
		}else {
			result = ls.delFeedBack(ids);
		}
		if(result>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "操作成功！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "操作失败！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/api/info",method=RequestMethod.GET)
	public Map getInfo(String page,String limit,String keyword,String start,String end) {
		List list = null;
		int intpage=0,intlimit=0;
		long longstart=0,longend=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(limit)) { //page 和 limit必须同时传入 否则置0查询全部
			intpage = Integer.parseInt(page);
			intlimit = Integer.parseInt(limit);
		}
		keyword = StringUtils.isNotEmpty(keyword) ? keyword:null; //keyword非空为模糊查询
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			longstart = Long.parseLong(start);
			longend = Long.parseLong(end);
		}
		list = ls.getInfo(intpage, intlimit, longstart, longend, keyword);
		if(list!=null && list.size()>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
			map.put("count",ms.getLastCount());
			map.put("data", list);
		} else {
			map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无记录");
		}
		return map;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/api/info",method=RequestMethod.DELETE)
	public Map deleteInfo(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(StringUtils.isEmpty(ids)) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
		}else {
			result = ls.delInfo(ids);
		}
		if(result>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "操作成功！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "操作失败！");
		}
		return map;
		
	}
	/**   
	 * @Title: getMusic   
	 * @Description: 获取库中音乐列表
	 * @param page 页数 可选
	 * @param limit	单页数量 可选
	 * @param keyword 模糊搜索关键词 可选
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/api/music",method=RequestMethod.GET)
	public Map<String, Object> getMusic(String page,String limit,String keyword) {
		List<Object> list = null;
		int intpage=0,intlimit=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(limit)) { //page 和 limit必须同时传入 否则置0查询全部
			intpage = Integer.parseInt(page);
			intlimit = Integer.parseInt(limit);
		}
		
		keyword = StringUtils.isNotEmpty(keyword) ? keyword:null; //keyword非空为模糊查询
		list = ms.getMusic(intpage, intlimit, keyword);
		
		if(list!=null && list.size()>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
			map.put("count",ms.getLastCount());
		} else {
			map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无记录！");
			map.put("count", 0);
		}
		map.put("data", list);
		return map;
	}
	
	/**   
	 * @Title: setCommend   
	 * @Description: 修改推荐状态
	 * @param commend 推荐状态   Y or N 必选
	 * @param songids id文本 "1" or "1,2,3" 必选 
	 * @return 操作结果JSON文本
	 */
	@ResponseBody
	@RequestMapping(value="/api/music",method=RequestMethod.PUT)
	public Map setCommend(String commend,String songids) { //commend:"Y" or "N" songids:id文本 "1" or "1,2,3"
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(songids) || StringUtils.isEmpty(commend)) {
			map = this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
		} else {
			int updatecount = ms.setCommend(commend, songids);
			if(updatecount<=0) map = this.setStatus(map, STATUS_CODE_ERROR, "设置失败！请查看日志！");
			else map = this.setStatus(map, STATUS_CODE_SUCCESS, "设置成功！");
		}
		return map;
	}
	
	/**   
	 * @Title: delMusic   
	 * @Description: 删除音乐
	 * @param ids id文本 "1" or "1,2,3" 必选
	 * @return 操作结果JSON文本
	 */
	@ResponseBody
	@RequestMapping(value="/api/music",method=RequestMethod.DELETE)
	public Map delMusic(String ids) { //id文本 "1" or "1,2,3"
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(StringUtils.isEmpty(ids)) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
		}else {
			result = ms.deleteByIds(ids);
		}
		if(result>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "操作成功！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "操作失败！");
		}
		return map;
	}
	
	private HashMap<String, Object> setStatus(HashMap map,int statuscode,String msg) {
		map.put("status", statuscode);
		map.put("msg",msg);
		return map;
	}
}
