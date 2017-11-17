package music;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daylyweb.music.mapper.FeedBack;
import com.daylyweb.music.mapper.Info;
import com.daylyweb.music.mapper.Zan;
import com.daylyweb.music.service.LogService;
import com.daylyweb.music.service.MusicService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/log")
public class LogApi {
	
	private final int STATUS_CODE_SUCCESS=200;
	private final int STATUS_CODE_ERROR=500;
	private final int STATUS_CODE_NOT_FOUND=404;
	
	@Autowired
	private LogService ls;
	@Autowired
	private MusicService ms;
	
	static Logger logger = LogManager.getLogger(LogApi.class);
	
	@ResponseBody
	@RequestMapping(value="/zan",method=RequestMethod.GET)
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
	@RequestMapping(value="/zan",method=RequestMethod.POST)
	public Map insertZan(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Zan zan = new Zan();
		zan.setIp(request.getRemoteHost());
		zan.setDevice(request.getHeader("User-Agent"));
		zan.setTime(new Date(new java.util.Date().getTime()));
		int count = ls.insertZan(zan);
		if(count>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "感谢您的支持，您的支持便是最大的动力！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "虽然点赞失败辣，但还是很谢谢您的支持！可以尝试重新点一下哦！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/zan",method=RequestMethod.DELETE)
	public Map deleteZan(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(StringUtils.isEmpty(ids)) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
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
	@RequestMapping(value="/feedback",method=RequestMethod.GET)
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
	@RequestMapping(value="/feedback",method=RequestMethod.POST)
	public Map insertFeedBack(String name,String concat,String device,String content) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		FeedBack fb = new FeedBack(name,concat,device,content,new Date(new java.util.Date().getTime()));
		int count = ls.insertFeedBack(fb);
		if(count>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "感谢您的反馈，我们会听取您的建议！");
		} else {
			map=this.setStatus(map, STATUS_CODE_ERROR, "虽然反馈失败辣，但还是很谢谢您的支持！可以尝试重新反馈一下哦！");
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/feedback",method=RequestMethod.DELETE)
	public Map deleteFeedBack(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result=0;
		if(StringUtils.isEmpty(ids)) {
			map=this.setStatus(map, STATUS_CODE_ERROR, "参数错误！");
			return map;
		} else {
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
	@RequestMapping(value="/info",method=RequestMethod.GET)
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
	@RequestMapping(value="/info",method=RequestMethod.DELETE)
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
	
	private HashMap<String, Object> setStatus(HashMap map,int statuscode,String msg) {
		map.put("status", statuscode);
		map.put("msg",msg);
		return map;
	}
}
