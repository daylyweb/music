package com.daylyweb.music.service;

import java.util.List;

import com.daylyweb.music.mapper.FeedBack;
import com.daylyweb.music.mapper.Zan;

public interface LogService {
	List getZan(int page,int limit,long longstart,long longend,String keyword);
	int insertZan(Zan zan);
	int delZan(String ids);
	List getFeedBack(int page,int limit,long longstart,long longend,String keyword);
	int insertFeedBack(FeedBack feedback);
	int delFeedBack(String ids);
	List getInfo(int page,int limit,long longstart,long longend,String keyword);
	int delInfo(String ids);
}
