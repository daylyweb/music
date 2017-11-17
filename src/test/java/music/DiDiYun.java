package music;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Test;

public class DiDiYun {
	URLConnection con;
	String cookies,ticket;
	int uid;
	String phone,email,password;
	public boolean sendCode() {
		PrintWriter pw=null;
		
		try {
			URL url = new URL("https://epassport.diditaxi.com.cn/passport/login/smsget");
			con = url.openConnection();
			con.setDoOutput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json, text/plain, */*");
			con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
			con.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
			con.setRequestProperty("Connection","keep-alive");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Host","epassport.diditaxi.com.cn");
			con.setRequestProperty("Origin","https://app.didiyun.com");
			con.setRequestProperty("Referer","https://app.didiyun.com/");
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			JSONObject jj = new JSONObject();
			jj.put("cell", phone);
			jj.put("email", StringEscapeUtils.unescapeHtml4(email));
			jj.put("role", 31);
			jj.put("smstype", 0);
			String para = new String(("q="+jj.toString()).getBytes(),"UTF-8");
			pw = new PrintWriter(con.getOutputStream());
			pw.print(para);
			pw.flush();
			System.out.println(cookies);
			String back = readStr(con.getInputStream());
			System.out.println("发送验证码返回："+back);
			JSONObject jo = new JSONObject(back);
			String errmsg = jo.getString("errmsg");
			int errno = jo.getInt("errno");
			if(StringUtils.isNotEmpty(errmsg) && errno==0) return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
		
        //使用finally块来关闭输出流、输入流
        finally{
            if(pw!=null){
				pw.close();
			}
        }
		return false;
	}
	
	public boolean signUp1(String code) {
		PrintWriter pw=null;
		HttpURLConnection con;
		try {
			
			URL url = new URL("https://epassport.diditaxi.com.cn/passport/login/v3/signup");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json, text/plain, */*");
			con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
			con.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
			con.setRequestProperty("Connection","keep-alive");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestProperty("Host","epassport.diditaxi.com.cn");
			con.setRequestProperty("Origin","https://app.didiyun.com");
			con.setRequestProperty("Referer","https://app.didiyun.com/");
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			JSONObject jj = new JSONObject();
			jj.put("cell", phone);
			jj.put("password",password);
			jj.put("code", code);
			jj.put("role", 31);
			jj.put("scene", 16);
			String para = new String(("q="+jj.toString()).getBytes(),"UTF-8");
			pw = new PrintWriter(con.getOutputStream());
			pw.print(para);
			pw.flush();
			con.connect();
			String back = readStr(con.getInputStream());
			System.out.println("发送邮箱返回："+back);
			JSONObject jo = new JSONObject(back);
			ticket = jo.getString("ticket");
			uid = jo.getInt("uid");
			if(StringUtils.isNotEmpty(ticket) && uid!=0) return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
		
        //使用finally块来关闭输出流、输入流
        finally{
            if(pw!=null){
				pw.close();
			}
        }
		return false;
		
	}
	
	public boolean signUp2() {
		PrintWriter pw=null;
		HttpURLConnection con;
		try {
			
			URL url = new URL("https://api.didiyun.com/dicloud/api/user/signup?q="+ticket);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("OPTIONS");
			con.setDoOutput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Accept","*/*");
			con.setRequestProperty("Accept-Encoding","sdch,gzip, deflate, br");
			con.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
			con.setRequestProperty("Connection","keep-alive");
			con.setRequestProperty("Host","api.didiyun.com");
			con.setRequestProperty("Origin","https://app.didiyun.com");
			con.setRequestProperty("Referer","https://app.didiyun.com/");
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			con.setRequestProperty("Access-Control-Request-Headers", "authorization, content-type");
			con.setRequestProperty("Access-Control-Request-Method", "POST");
			con.connect();
			String back = readStr(con.getInputStream());
			cookies = con.getHeaderField("set-cookie");
			System.out.println("signup2返回："+back+"\tcookies:"+cookies);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
		
        //使用finally块来关闭输出流、输入流
        finally{
            if(pw!=null){
				pw.close();
			}
        }
		return false;
	}
	
	public boolean signUp3() {
		PrintWriter pw=null;
		HttpURLConnection con;
		try {
			//URL url = new URL("https://epassport.diditaxi.com.cn/passport/login/v3/signup?q=pjToBlZtzTfIm7krdHA054BFon4Pjj1_lsESc42UqmBMzTkOwkAMQNGroF-7sOMxE_s2LGEpEBIjqih3p6BJ-aq3cqJAOFNzb-52jNTJQ60LV8qEhVoPjPf3c1koFcZrUBamPmWLtgm3vYU7hUVaT_fQGeHxX56Ubr8AAAD__w==");
			URL url = new URL("https://api.didiyun.com/dicloud/api/user/signup?q="+ticket);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Accept", "application/json, text/plain, */*");
			con.setRequestProperty("Accept-Encoding","gzip, deflate, br");
			con.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
			con.setRequestProperty("Connection","gzip, deflate, br");
			con.setRequestProperty("Authorization","Token "+ticket);
			con.setRequestProperty("Connection","keep-alive");
			con.setRequestProperty("Content-Type","application/json;charset=UTF-8");
			con.setRequestProperty("Host","api.didiyun.com");
			con.setRequestProperty("Origin","https://app.didiyun.com");
			con.setRequestProperty("Referer","https://app.didiyun.com/");
			con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			cookies = StringUtils.isNotEmpty(cookies) ? cookies:"_ga=GA1.2.529679366.1510324358; _gid=GA1.2.1307899803.1510324358";
			con.setRequestProperty("cookie", cookies);
			JSONObject jj = new JSONObject();
			jj.put("invite", "2d7ziDFNqwr");
			jj.put("email",StringEscapeUtils.unescapeHtml4(email));
			String para = new String(jj.toString().getBytes(),"UTF-8");
			pw = new PrintWriter(con.getOutputStream());
			pw.print(para);
			pw.flush();
			con.connect();
			String back = readStr(con.getInputStream());
			System.out.println("signup3返回："+back+"\tcookies:"+cookies);
			JSONObject jo = new JSONObject(back);
			String errmsg = jo.getString("errmsg");
			int errno = jo.getInt("errno");
			if(StringUtils.isNotEmpty(errmsg) && errno==0) return true;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("失败！");
		}
		
        //使用finally块来关闭输出流、输入流
        finally{
            if(pw!=null){
				pw.close();
			}
        }
		return false;
	}
	
	@Test
	public void diDiYunRegist() {
		phone="18476429795";
		email="18344103036@163.com";
		password="QWEad19970620";
		if(sendCode()) {
			System.out.println("请输入手机验证码：");
			Scanner sc = new Scanner(System.in);
			String code = sc.nextLine();
			if(StringUtils.isNotEmpty(code)) {
				if(signUp1(code)) {
					if(signUp3()) {
						System.out.println("注册成功！请到："+email+"查看验证邮件");
					};
				}
			}
		}
	}
	
	public String readStr(InputStream is) {
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
}
