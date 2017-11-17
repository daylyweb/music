package com.daylyweb.music.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daylyweb.music.dao.MusicDao;
import com.daylyweb.music.mapper.Music;

/**   
 * @ClassName:  MusicService   
 * @Description: 对数据库内音乐操作的业务层
 * @author: 丶Tik 
 * @date:   2017年11月7日 下午8:50:03     
 *  
 */ 
public interface MusicService {
	
	/**   
	 * @Title: keywordIsExist   
	 * @Description: 对应关键词是否有记录
	 * @param keyword 关键词
	 * @return 
	 */
	boolean keywordIsExist(String keyword);
	
	/**   
	 * @Title: insert   
	 * @Description: 插入一条音乐数据  
	 * @param music 音乐实体类
	 * @return 返回受影响行数<=0为插入失败
	 */
	int insert(Music music);
	
	/**   
	 * @Title: insert   
	 * @Description: 插入一组音乐数据 
	 * @param list 音乐实体类的List集合
	 * @return 返回受影响行数<=0为插入失败
	 */
	int insert(List list);
	
	/**   
	 * @Title: delete   
	 * @Description: 删除一条音乐数据
	 * @param songid 需要删除的songid
	 * @return 返回受影响行数<=0为删除失败
	 */
	int delete(int songid);
	
	/**   
	 * @Title: deleteByIds   
	 * @Description: 删除一组音乐数据
	 * @param songids id文本   1,2,3,4 where id in(songids)
	 * @return 返回受影响行数<=0为删除失败
	 */
	int deleteByIds(String songids);
	
	/**   
	 * @Title: setCommend   
	 * @Description: 设置音乐数据的推荐状态
	 * @param commend 非空 "Y" or "N"
	 * @param songids	id文本   1,2,3,4 where id in(songids)
	 * @return 返回受影响行数<=0为设置失败
	 */
	int setCommend(String commend,String songids);
	
	/**   
	 * @Title: getById   
	 * @Description: 通过songid获取音乐数据
	 * @param songid 没什么好说的 就是songid
	 * @return 返回音乐信息的实体类
	 */
	Music getById(int songid);
	
	/**   
	 * @Title: getMusic   
	 * @Description: 获取音乐列表
	 * @param page 页数	置0返回全部 非0分页返回
	 * @param limit 一页音乐数量	置0返回全部 非0分页返回
	 * @param keyword 模糊查询关键词 置null为非模糊查询   模糊查询内容包括songid,songmid,songname,albumname,singer等字段
	 * @return 返回音乐列表
	 */
	List getMusic(int page,int limit,String keyword);
	
	/**   
	 * @Title: getCommend   
	 * @Description: 获取推荐音乐列表 
	 * @param page	页数	置0返回全部 非0分页返回
	 * @param limit	一页音乐数量	置0返回全部 非0分页返回
	 * @param commend	推荐状态    "Y" or "N"
	 * @param keyword 模糊查询关键词 置null为非模糊查询  模糊查询内容包括songid,songmid,songname,albumname,singer等字段
	 * @return 返回音乐列表
	 */
	List getCommend(int page,int limit,String commend,String keyword);
	
	/**   
	 * @Title: update   
	 * @Description: 根据songid更新音乐数据
	 * @param music 音乐信息实体类
	 * @return 返回受影响行数<=0为设置失败
	 */
	int update(Music music);
	
	
	/**   
	 * @Title: getCount   
	 * @Description: 获取包含SQL_CALC_FOUND_ROWS的上次查询后统计的总数  
	 * @return 返回上次查询的统计总数
	 */
	int getLastCount();
}
