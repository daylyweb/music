package com.daylyweb.music.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.daylyweb.music.mapper.Music;

/**   
 * @ClassName:  MusicUtil   
 * @Description:音乐工具类
 * @author: 丶Tik 
 * @date:   2017年10月17日 上午1:25:54     
 *  
 */ 
@Component(value="MusicUtil")
@Scope("prototype")
public class MusicUtil extends Exception{
	
	private List<Music> musics = new ArrayList<Music>();
	private String keyword=null;
	private Insert2DB i2d=null;
	
	private final String musicurl="https://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp?format=json&inCharset=utf-8&outCharset=utf-8&n={count}&p={page}&w={keyword}";
	private final String referer="https://c.y.qq.com/v8/playsong.html?songmid={songmid}&ADTAG=myqq&from=myqq";
	private final String lrcUrl="https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric.fcg?nobase64=1&musicid={songid}&songtype=0";
	
/*	String albumImg = "https://y.gtimg.cn/music/photo_new/T002R500x500M000"+albummid+".jpg";
	String infoUrl = "https://c.y.qq.com/v8/playsong.html?songmid="+songmid;
	String m4aUrl = "http://ws.stream.qqmusic.qq.com/"+songid+".m4a?fromtag=46";
	String albumImg = "http://imgcache.qq.com/music/photo/mid_album_90/"+albummid.charAt(albummid.length()-2)+"/"+albummid.charAt(albummid.length()-1)+"/"+albummid+".jpg";    小专辑封面 90*90
	String lrc = "http://music.qq.com/miniportal/static/lyric/"+songid%100+"/"+songid+".xml"; */
	
	public MusicUtil() {}
	public MusicUtil(String keyword) {
		this.keyword=keyword;
	}
	
	/**   
	 * @Title: getAllMusic   
	 * @Description: TODO     
	 * @param keyword
	 * @return	list 音乐列表list
	 * @throws Exception 
	 */
	public List getAllMusic(String keyword) throws Exception {
		this.setKeyword(keyword);
		return this.getAllMusic();
	}
	
	/**   
	 * @Title: getAllMusic   
	 * @Description: 根据搜索关键词获取音乐列表
	 * @param reload 是否重载  true重新调用接口获取 false直接返回对象中已经存在的音乐list
	 * @return list 音乐列表list
	 * @throws Exception 
	 */
	public List getAllMusic(boolean reload) throws Exception {
		if(reload) return this.getAllMusic();
		return musics;
	}
	
	/**   
	 * @Title: getAllMusic   
	 * @Description: 根据搜索关键词获取音乐列表
	 * @return	list 音乐列表list
	 * @throws Exception 
	 */
	private List getAllMusic() throws Exception {
		if(keyword==null || "".equals(keyword)) {throw new Exception("关键词为空，请先调用setKeyword()设置关键词");}
		int size=0,page=1;
		musics.clear();
		do {
				List<Music> list = this.getMusic(page, 30);
				size=list.size();
				musics.addAll(list);
				if(size<30) return musics;
				page++;
		} while(true);
		
	}
	
	/**   
	 * @Title: getMusic   
	 * @Description: 分页显示歌曲列表
	 * @param page	页数
	 * @param pagecount	单页数量
	 * @param reload 是否重载  true重新调用接口获取 false直接从对象中已经存在的音乐list里获取
	 * @return
	 * @throws Exception 
	 */
	public List getMusic(int page,int pagecount,boolean reload) throws Exception {
		if(reload) {
			return this.getMusic(page,pagecount);
		} else {
			int fromIndex=(page-1)*pagecount;
			int toIndex=page*pagecount-1;
			return new ArrayList<Music>(musics.subList(fromIndex, toIndex));
		}
	}

	/**   
	 * @Title: getMusic   
	 * @Description: 按页数 和 单页数量 返回音乐List集合  
	 * @param page	页数
	 * @param pagecount	单页数量
	 * @return	List  音乐List
	 * @throws Exception 
	 */
	private List<Music> getMusic(int page,int pagecount) throws Exception {
		if(keyword==null || "".equals(keyword)) {throw new Exception("关键词为空，请先调用setKeyword()设置关键词");}
		String pagestr = String.valueOf(page);
		String countstr = String.valueOf(pagecount);
		List<Music> list = new ArrayList<Music>();
		String str = StringEscapeUtils.unescapeHtml4(this.getMusicJson(keyword, pagestr, countstr));
		JSONObject jo = new JSONObject(str);
		jo = jo.getJSONObject("data");
		jo = jo.getJSONObject("song");
		JSONArray ja = jo.getJSONArray("list");
		for(int i=0,h=ja.length();i<h;i++){
			JSONObject jotemp = (JSONObject) ja.get(i);
			JSONArray singerarr = jotemp.getJSONArray("singer");
			StringBuffer sb= new StringBuffer();
			for(int a=0,c=singerarr.length();a<c;a++) {
				JSONObject sgo = (JSONObject) singerarr.get(a);
				sb.append(sgo.getString("name"));
				if(a<c-1) sb.append(",");
			}
			
			String singer = sb.toString();
			String songName = jotemp.getString("songname");
			String albumName = jotemp.getString("albumname");
			String albummid = jotemp.getString("albummid");
			int songid=jotemp.getInt("songid");
			String songmid = jotemp.getString("songmid");
			//System.out.println("歌名："+songName+"\t歌手："+singer+"\t专辑封面："+albumImg+"\t歌曲信息:"+infoUrl+"\t歌曲文件直链："+m4aUrl+"\t专辑名："+albumName+"\t歌词地址："+lrc);
/*			Map map = new HashMap();
	    	map.put("songid",songid);
	    	map.put("songmid",songmid);
	    	map.put("songname",songName);
	    	map.put("singer",singer);
	    	map.put("albumname",albumName);
	    	map.put("albummid",albummid);
	    	map.put("commend","N");
	    	map.put("keyword",keyword);
	    	musics.add(map);*/
			list.add(new Music(songid,songmid,songName,singer,albumName,albummid,"N",keyword));
		}
		if(i2d!=null) i2d.insert(list);
		return list;
	}
	
	
	/**   
	 * @Title: getMusicJson   
	 * @Description: 根据关键词 页数 单页数量获取音乐Json 可自行解析 
	 * @param keyword 音乐关键词
	 * @param page	页数
	 * @param count 单页数量
	 * @return QQ音乐返回的json 可自行解析
	 * @throws Exception 
	 */
	public String getMusicJson(String keyword,String page,String count) throws Exception {
		if(keyword==null || "".equals(keyword) || page==null || "".equals(page) || count==null || "".equals(count)) throw new Exception ("参数不能为空！");
		String newurl = musicurl.replace("{page}", page).replace("{count}", count).replace("{keyword}", java.net.URLEncoder.encode(keyword,"UTF-8"));
		URL url = new URL(newurl);
		URLConnection con = url.openConnection();
		con.setConnectTimeout(6000);
		con.connect();
		return readStr(con.getInputStream());
	}
	
	/**   
	 * @Title: getLrc   
	 * @Description: 通过对象中音乐集合的索引来获取歌词
	 * @param index 对象中音乐集合的索引
	 * @return	List  返回带有time lrc的 map集合
	 * @throws JSONException
	 * @throws Exception 
	 */
	public List getLrc(int index) throws JSONException, Exception {
		if(index<0 || musics.isEmpty() || index>=musics.size()) throw new Exception ("索引越界 或 音乐列表为空！");
		Music music = musics.get(index);
		return this.getLrc(String.valueOf(music.getSongid()), music.getSongmid());
	}
	
	/**
	 * @Title: getLrc   
	 * @Description: 通过songid+mid请求QQ音乐接口获取歌词
	 * @param songid 歌曲id
	 * @param songmid 歌曲mid
	 * @return	List  返回带有time lrc的 map集合
	 * @throws Exception 
	 * @throws JSONException  
	 */
	public List getLrc(String songid,String songmid) throws JSONException, Exception{
		List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
	    String result = null;
	    JSONObject jo1= new JSONObject(this.getLrcJson(songid, songmid));
		 String lrc = StringEscapeUtils.unescapeHtml4(jo1.getString("lyric"));
		 Pattern pattern =Pattern.compile("\\[\\d+:\\d+(\\.|:)\\d+\\].*");
		 Matcher matcher = pattern.matcher(lrc);
		 float time=0.00f;
		 while(matcher.find())
	 	 {
			String[] str=matcher.group().split("\\]",2);
			if(str.length==2)
			{
				str[0] = str[0].replaceFirst("\\[", "");
				time= Float.parseFloat(str[0].substring(0, 2))*60+Float.parseFloat(str[0].substring(3).replaceFirst(":", "."));
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("time", time);
				map.put("lrc", str[1]);
				list.add(map);
			}
		 }
		return list;
	}
	
	/**   
	 * @Title: getLrcJson   
	 * @Description: 获取歌词接口返回的字符串 一般是 callback(Json) 可获取完另行解析
	 * @param songid	歌曲id
	 * @param songmid  歌曲mid
	 * @return	String  返回 去除回调的json内容 可自行解析
	 * @throws Exception
	 */
	public String getLrcJson(String songid,String songmid) throws Exception {
		String result=null;
	    URL url = new URL(lrcUrl.replace("{songid}", songid));
	    URLConnection connection = url.openConnection();
	    connection.addRequestProperty("referer", referer.replace("{songmid}", songmid));
	    result = readStr(connection.getInputStream());
	    if(result!=null) result=result.substring(result.indexOf("(")+1, result.length()-1);
	    return result;
	}
	
	/**   
	 * @Title: readStr   
	 * @Description: 从inputstream输入流中读取出文本 通常为json   
	 * @param is 输入流  
	 * @return String  读出的文本
	 */
	private String readStr(InputStream is) {
		try {
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			char[] buffer = new char[1024];
			StringBuffer sb = new StringBuffer();
			for(;;) 
			{
				int rsz = isr.read(buffer, 0, buffer.length);
				if (rsz < 0)  break;
			    sb.append(buffer, 0, rsz);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: setInsert2DB   
	 * @Description: 设置搜索歌曲时 插入数据库的实现方法  设为null可取消存入数据库 
	 * @param insert2db 搜索歌曲时 插入数据库的实现方法 请实现com.daylyweb.music.utils.MusicUtil.Insert2DB 该接口
	 */
	public void setInsert2DB(MusicUtil.Insert2DB insert2db) {
		this.i2d=insert2db;
	}
	
	/**   
	 * @Title: setKeyword   
	 * @Description: 设置音乐搜索关键词
	 * @param keyword 音乐搜索关键词
	 */
	public void setKeyword(String keyword) {
			this.keyword=keyword;
	}
	
	public interface Insert2DB{
			public int insert(List list);
	}
}


