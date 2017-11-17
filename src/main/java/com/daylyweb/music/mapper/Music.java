package com.daylyweb.music.mapper;

import java.io.Serializable;
import java.util.Map;

/**   
 * @ClassName:  Music   
 * @Description:TODO
 * @author: 丶Tik 
 * @date:   2017年10月15日 下午6:18:29     
 *  
 */ 
public class Music implements Serializable{
    /**   
	 * @Fields serialVersionUID : TODO
	 *  
	 */ 
	private static final long serialVersionUID = 1L;

	private Integer songid;

    private String songmid;

    private String songname;

    private String singer;

    private String albumname;

	private String albummid;

    private String commend;
    
    private String keyword;

    public Music() {}
    

    /**   
     * @Title:	Music   
     * @Description:	构造方法
     * @param songid 歌曲id
     * @param songmid 歌曲mid
     * @param songname 歌曲名
     * @param singer 歌手
     * @param albumname 专辑名
     * @param albummid 专辑mid
     * @param commend 是否推荐 Y N
     * @param keyword 歌曲关键词
     * @throws	
     *
     */ 
    public Music(Integer songid,String songmid,String songname,String singer,String albumname,String albummid,String commend,String keyword) {
    	this.setSongid(songid);
    	this.setSongmid(songmid);
    	this.setSongname(songname);
    	this.setSinger(singer);
    	this.setAlbumname(albumname);
    	this.setAlbummid(albummid);
    	this.setCommend(commend);
    	this.setKeyword(keyword);
    }
    
    public Music(Map map) {
    	Object songid = map.get("songid");
    	Object songmid = map.get("songmid");
    	Object songname = map.get("songname");
    	Object singer = map.get("singer");
    	Object albumname = map.get("albumname");
    	Object albummid = map.get("albummid");
    	Object commend = map.get("commend");
    	Object keyword = map.get("keyword");
    	this.songid=(songid!=null?(int) songid:0);
    	this.songmid=(songmid!=null?(String) songmid:"");
    	this.songname=(songname!=null?(String) songname:"");
    	this.singer=(singer!=null?(String) singer:"");
    	this.albumname=(albumname!=null?(String) albumname:"");
    	this.albummid=(albummid!=null?(String) albummid:"");
    	this.commend=(commend!=null?(String) commend:"N");
    	this.keyword=(keyword!=null?(String) keyword:"");
    }
    public Integer getSongid() {
        return songid;
    }

    public void setSongid(Integer songid) {
        this.songid = songid;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid == null ? null : songmid.trim();
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname == null ? null : songname.trim();
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer == null ? null : singer.trim();
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname == null ? null : albumname.trim();
    }

    public String getCommend() {
        return commend;
    }
    
    public String getAlbummid() {
		return albummid;
	}

	public void setAlbummid(String albummid) {
		this.albummid = albummid == null ? null:albummid.trim();
	}

    public void setCommend(String commend) {
        this.commend = commend == null ? null : commend.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }
}