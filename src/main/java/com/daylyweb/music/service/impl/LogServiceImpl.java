package com.daylyweb.music.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daylyweb.music.dao.FeedBackDao;
import com.daylyweb.music.dao.InfoDao;
import com.daylyweb.music.dao.ZanDao;
import com.daylyweb.music.mapper.FeedBack;
import com.daylyweb.music.mapper.Zan;
import com.daylyweb.music.service.LogService;
import com.daylyweb.music.service.MusicService;

/**   
 * @ClassName:  LogServiceImpl   
 * @Description:TODO
 * @author: 丶Tik 
 * @date:   2017年10月22日 下午11:11:24     
 *  
 */ 
@Service("LogService")
public class LogServiceImpl implements LogService {

	@Autowired
	private ZanDao zd;
	@Autowired
	private FeedBackDao fbd;
	@Autowired
	private InfoDao infodao;
	
	public List getZan(int page,int limit,long longstart,long longend,String keyword) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(page>0 && limit >0){
			int start = (page-1)*limit;
			map.put("start", start);
			map.put("limit", limit);
		}
		if(longstart>0 && longend>0) {
			map.put("timestart", new Date(longstart));
			map.put("timeend", new Date(longend));
		}
		map.put("keyword", keyword);
		return zd.select(map);
	}

	public int insertZan(Zan zan) {
		return zd.insert(zan);
	}
	
	public int delZan(String ids) {
		if("all".equals(ids.toLowerCase())){
			return zd.deleteAll()+1;
		} else {
			return zd.delete(ids);
		}
	}
	
	public List getFeedBack(int page,int limit,long longstart,long longend,String keyword) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(page>0 && limit >0){
			int start = (page-1)*limit;
			map.put("start", start);
			map.put("limit", limit);
		}
		if(longstart>0 && longend>0) {
			map.put("timestart", new Date(longstart));
			map.put("timeend", new Date(longend));
		}
		map.put("keyword", keyword);
		return fbd.select(map);
	}

	public int insertFeedBack(FeedBack feedback) {
		return fbd.insert(feedback);
	}

	public int delFeedBack(String ids) {
		if("all".equals(ids.toLowerCase())){
			return fbd.deleteAll()+1;
		} else {
			return fbd.delete(ids);
		}
	}

	public List getInfo(int page,int limit,long longstart,long longend,String keyword) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(page>0 && limit >0){
			int start = (page-1)*limit;
			map.put("start", start);
			map.put("limit", limit);
		}
		if(longstart>0 && longend>0) {
			map.put("timestart", new Date(longstart));
			map.put("timeend", new Date(longend));
		}
		map.put("keyword", keyword);
		return infodao.select(map);
	}

	public int delInfo(String ids) {
		if("all".equals(ids.toLowerCase())){
			return infodao.deleteAll()+1;  //truncate清空表成功返回0 失败返回-1
		} else {
			return -1;
		}
	}
	
	
}
