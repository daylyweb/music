package com.daylyweb.music.dao;

import com.daylyweb.music.mapper.Admin;

public interface AdminDao {
	Admin getByUserName(String username);
	
    int insert(Admin record);

    int insertSelective(Admin record);
    
    int update(Admin admin);
}