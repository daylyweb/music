package com.daylyweb.music.dao;

import java.util.List;
import java.util.Map;

import com.daylyweb.music.mapper.Zan;

public interface ZanDao {
	
	List select(Map map);
	
    int delete(String ids);

    int deleteAll();
    
    int insert(Zan zan);

}