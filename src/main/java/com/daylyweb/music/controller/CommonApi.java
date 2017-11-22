package com.daylyweb.music.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daylyweb.music.mapper.FeedBack;
import com.daylyweb.music.mapper.Music;
import com.daylyweb.music.mapper.Zan;
import com.daylyweb.music.service.LogService;
import com.daylyweb.music.service.MusicService;
import com.daylyweb.music.utils.MusicUtil;
import com.daylyweb.music.utils.MusicUtil.Insert2DB;

/**   
 * @ClassName:  CommonApi   
 * @Description:	前台api
 * @author: 丶Tik 
 * @date:   2017年11月22日 下午1:29:37     
 *  
 */ 
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class CommonApi {
	
	private final int STATUS_CODE_SUCCESS=200;
	private final int STATUS_CODE_ERROR=500;
	private final int STATUS_CODE_NOT_FOUND=404;
	@Autowired
	private MusicUtil mu;
	@Autowired
	private MusicService ms;
	@Autowired
	private LogService ls;
	
	/**   
	 * @Title: musicSearch   
	 * @Description: 搜索歌曲
	 * @param keyword 歌曲关键词
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/search/{keyword}",method=RequestMethod.GET)
	public Map<String, Object> musicSearch(@PathVariable("keyword") String keyword) {
		mu.setKeyword(keyword);
		if (ms.keywordIsExist(keyword)==false) {
			mu.setInsert2DB(new Insert2DB() {
				public int insert(List list) {
					return ms.insert(list);
				}
			});
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List list = mu.getAllMusic(true);
			if(list!=null && list.size()>0) {
				map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
				map.put("data",list);
			} else {
				this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无此歌曲！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map=this.setStatus(map,this.STATUS_CODE_ERROR,e.getMessage());
		}
		return  map;
	}
	
	/**   
	 * @Title: getLrc   
	 * @Description: 获取歌词
	 * @param songid 歌曲id
	 * @param songmid 歌曲mid
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/lrc/{songid}/{songmid}",method=RequestMethod.GET)
	public Map<String, Object> getLrc(@PathVariable("songid") String songid,@PathVariable("songmid") String songmid){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", "fail");
		try {
			List<?> list = mu.getLrc(songid, songmid);
			if(list!=null && list.size()>0) {
				map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
				map.put("data",list);
			} else {
				map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "该歌曲暂无歌词！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map=this.setStatus(map,this.STATUS_CODE_ERROR,e.getMessage());
		}
		return map;
		
	}
	
	/**   
	 * @Title: musicCommend   
	 * @Description: 获取库中推荐列表
	 * @param page 页数 可选
	 * @param limit 单页数量 可选
	 * @param keyword 模糊搜索关键词 可选
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/commend",method=RequestMethod.GET)
	public Map musicCommend(String page,String limit,String keyword) {
		List list=null;
		int intpage=0,intlimit=0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(limit)) { //page 和 limit必须同时传入 否则置0查询全部
			intpage = Integer.parseInt(page);
			intlimit = Integer.parseInt(limit);
		}
		
		keyword = StringUtils.isNotEmpty(keyword) ? keyword:null; //keyword非空为模糊查询
		list=ms.getCommend(intpage, intlimit, "Y", keyword);

		if(list!=null && list.size()>0) {
			map=this.setStatus(map, STATUS_CODE_SUCCESS, "success");
			map.put("count",list.get(list.size()-1));
			list.remove(list.size()-1);
			map.put("data", list);
		} else {
			map=this.setStatus(map, STATUS_CODE_NOT_FOUND, "暂无推荐歌曲！");
			map.put("count",0);
		}
		return map;
	}
	
	/**   
	 * @Title: insertZan   
	 * @Description: 添加点赞记录
	 * @param request
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/zan",method=RequestMethod.POST)
	public Map insertZan(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		request.getSession().setAttribute("login", "丶Tik");
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
	
	/**   
	 * @Title: insertFeedBack   
	 * @Description: 添加反馈记录  
	 * @param name 姓名
	 * @param concat 联系方式
	 * @param device 设备型号
	 * @param content 反馈内容
	 * @return 
	 */
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
	
	/**   
	 * @Title: setStatus   
	 * @Description: 设置map json内容
	 * @param map 源map
	 * @param statuscode 状态码
	 * @param msg 信息
	 * @return map
	 */
	private HashMap<String, Object> setStatus(HashMap<String, Object> map,int statuscode,String msg) {
		map.put("status", statuscode);
		map.put("msg",msg);
		return map;
	}
}
