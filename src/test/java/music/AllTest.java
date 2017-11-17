package music;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.daylyweb.music.dao.MusicDao;
import com.daylyweb.music.mapper.Music;
import com.daylyweb.music.mapper.Zan;
import com.daylyweb.music.service.LogService;
import com.daylyweb.music.service.MusicService;
import com.daylyweb.music.utils.ConnectionFactory;
import com.daylyweb.music.utils.MusicUtil;
import com.daylyweb.music.utils.MusicUtil.Insert2DB;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mybatis.xml"})
public class AllTest {
	@Autowired
	private MusicService ms;
	@Autowired
	private MusicUtil mu;
	@Autowired
	private MusicDao md;
	@Autowired
	private LogService ls;
	
	Logger logger = LogManager.getLogger("info");

	public void test() {
		List<Music> list = null;
		Long starttime = System.currentTimeMillis();
		try {
			mu.setInsert2DB(new Insert2DB() {
				public void insert(List list) {
					ms.insert(list);
				}
			});
			list = mu.getAllMusic("白马照青衣");
			Music music = list.get(0);
			System.out.println(mu.getLrcJson(String.valueOf(music.getSongid()), music.getSongmid()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long endtime = System.currentTimeMillis();
		System.out.println(list.size()+"\t"+(endtime - starttime));
	}
	
	
	public void test2() {
		Zan zan = new Zan();
		zan.setIp("10.156.12.112");
		zan.setDevice("手机");
		zan.setTime(new Date(System.currentTimeMillis()));
		System.out.println(ls.insertZan(zan));
	}
	
	
	public void test4() {
		try {
			 String json = StringEscapeUtils.unescapeHtml4(mu.getLrcJson("102425374", "003QplkH3wXLIG"));
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void fuzzyquery(String keyword) {
		try {
			DataSource ds = ConnectionFactory.getDataSource();
			Connection con = ds.getConnection();
			Statement sm =con.createStatement();
			Statement sm1 =con.createStatement();
			ResultSet rs = sm.executeQuery("select SQL_CALC_FOUND_ROWS * from music_list WHERE CONCAT_WS(\"\",songid,songmid,songname,albumname,singer,keyword) LIKE '%"+keyword+"%' limit 0,10");
			int i=0;
			while(rs.next()) {
				i++;
			}
			rs.close();
			sm.close();
			con.close();
			System.out.println(i);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void getCount() {
		DataSource ds;
		try {
			ds = ConnectionFactory.getDataSource();
			Connection con = ds.getConnection();
			Statement sm =con.createStatement();
			ResultSet rs = sm.executeQuery("SELECT FOUND_ROWS()");
			while(rs.next()) {
				System.out.println(rs.getInt(1));
			}
			rs.close();
			sm.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void insert() {
		System.out.println(58  >> 2);
		Music m1 = new Music(102973042,"002NBfDi3QPu46","琴师","双笙","笙声不息","001Mf4Ic1aNYYK","Y","琴师");
		Music m2 = new Music(102973044,"000xKbLI2QEKE9","故梦","双笙","笙声不息","001Mf4Ic1aNYYK","Y","故梦");
		List<Music> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		System.out.println(ms.insert(list));
		System.out.println(ms.keywordIsExist("笙声不息"));
		System.out.println(ms.keywordIsExist("故梦"));
		System.out.println(ms.keywordIsExist("琴师"));
		System.out.println(ms.keywordIsExist("是"));
	}
}
