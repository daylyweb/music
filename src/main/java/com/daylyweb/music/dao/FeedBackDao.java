package com.daylyweb.music.dao;

import java.util.List;
import java.util.Map;

import com.daylyweb.music.mapper.FeedBack;

public interface FeedBackDao {

	List select(Map map);
	
	int getLastCount();
	 
    int delete(String ids);

    int deleteAll();
    
    int insert(FeedBack feedback);
}