package com.daylyweb.music.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.daylyweb.music.dao.MusicDao;
import com.daylyweb.music.mapper.Music;
import com.daylyweb.music.service.MusicService;

@Service("MusicService")
public class MusicServiceImpl implements MusicService{

	@Autowired
	private MusicDao musicdao;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	public int insert(Music music) {
		return musicdao.insert(music);
	}

	public int delete(int songid) {
		return 0;
	}

	public Music getById(int songid) {
		return musicdao.getById(songid);
	}

	public int update(Music music) {
		return 0;
	}

	public List<Object> getMusic(int page,int limit,String keyword) {
		SqlSession session = sqlSessionFactory.openSession(false);
		MusicDao md = session.getMapper(MusicDao.class);
		Map<String,Object> map = new HashMap<String, Object>();
		if(page>0 && limit >0){
			int start = (page-1)*limit;
			map.put("start", start);
			map.put("limit", limit);
		}
		List list = new ArrayList<>();
		if(StringUtils.isNotEmpty(keyword)) {
			map.put("keyword", keyword);
			list =  md.fuzzyQuery(map);
		} else {
			list =  md.select(map);
		}
		list.add(md.getLastCount());
		session.close();
		return list;
	}

	public List<Object> getCommend(int page,int limit,String commend,String keyword) {
		SqlSession session = sqlSessionFactory.openSession(false);
		MusicDao md = session.getMapper(MusicDao.class);
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(commend)) map.put("commend", commend);
		if(page>0 && limit >0){
			int start = (page-1)*limit;
			map.put("start", start);
			map.put("limit", limit);
		}
		List list = new ArrayList<>();
		if(StringUtils.isNotEmpty(keyword)) {
			map.put("keyword", keyword);
			list = md.fuzzyQuery(map);
		} else {
			list = md.select(map);
		}
		list.add(md.getLastCount());
		session.close();
		return list;
	}
	
	public int insert(List list) {
		if(list==null || list.size()<=0) return -1;
		return musicdao.insertByBatch(list);
	}

	public int setCommend(String commend,String songids) {
		if("y".equals(commend.toLowerCase())) {
			return musicdao.setCommend(songids);
		} else if("n".equals(commend.toLowerCase())){
			return musicdao.unCommend(songids);
		} else return -1;
		
	}

	public int deleteByIds(String songids) {
		return musicdao.deleteByIds(songids);
	}
	
	public int getLastCount() {
		return musicdao.getLastCount();
	}

	public boolean keywordIsExist(String keyword) {
		int result = musicdao.getKeywordCount(keyword);
		boolean flag = result>0 ? true:false;
		return flag;
	}
}
