package com.daylyweb.music.dao;

import java.util.List;
import java.util.Map;

import com.daylyweb.music.mapper.Info;

public interface InfoDao {
	List<Info> select(Map map);
	
	int getLastCount();
	
	int deleteAll();
}