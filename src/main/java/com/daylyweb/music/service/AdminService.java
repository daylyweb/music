package com.daylyweb.music.service;

import com.daylyweb.music.mapper.Admin;

public interface AdminService {
	Admin getByUserName(String username);
	boolean update(Admin admin);
}
