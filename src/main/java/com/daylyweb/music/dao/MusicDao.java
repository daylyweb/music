package com.daylyweb.music.dao;

import java.util.List;
import java.util.Map;

import com.daylyweb.music.mapper.Music;

public interface MusicDao {
	int insert(Music music);
	int insertByBatch(List<Object> list);
	int deleteByIds(String songids);
	Music getById(int songid);
	List<Object> select(Map<String,Object> map);
	List<Object> fuzzyQuery(Map<String,Object> map);
	int getKeywordCount(String keyword);
	int getLastCount();
	int setCommend(String ids);
	int unCommend(String ids);
	int update(Music music);
}
