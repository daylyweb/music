package com.daylyweb.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daylyweb.music.dao.AdminDao;
import com.daylyweb.music.mapper.Admin;
import com.daylyweb.music.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDao ad;
	
	public Admin getByUserName(String username) {
		return ad.getByUserName(username);
	}

	public boolean update(Admin admin) {
		boolean flag = ad.update(admin) == 1 ? true:false;
		return flag;
	}

}
