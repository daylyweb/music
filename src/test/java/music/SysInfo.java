package music;

import java.util.Properties;

import org.junit.Test;

public class SysInfo {
	@Test
	public void getSysInfo() {
		Properties prs = System.getProperties();
		Runtime rt = Runtime.getRuntime();
		System.out.println("运行环境:"+prs.getProperty("java.runtime.name"));
		System.out.println("Java Version:"+prs.getProperty("java.runtime.version"));
		System.out.println("系统版本:"+prs.getProperty("os.name")+" "+prs.getProperty("os.arch"));
		System.out.println("空闲内存:"+(rt.freeMemory()/1024L/1024L)+"mb");
		System.out.println("最大内存内存:"+(double)(rt.maxMemory()/1024/1024)+"mb");
		System.out.println("总内存内存:"+(double)(rt.totalMemory()/1024/1024)+"mb");
	}
}
