package com.ruin.masscan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruin.masscan.ExecuteCmd;

public class NmapAllCollector implements Runnable{
	
	private Semaphore semaphore;

	private String IP;

	private String ports;
	
	private int sum;

	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public String getIPS() {
		return IP;
	}

	public void setIPS(String iPS) {
		IP = iPS;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}
	
	public NmapAllCollector(Semaphore semaphore, String IP,int sum) {
		this.semaphore = semaphore;
		this.IP = IP;
		this.sum = sum;
	}
	
	@Override
	public void run() {
		
		try {
			// 获取 信号量 执行许可
			semaphore.acquire();
			Map<String, String> result = ExecuteCmd.execCmd("nmap "+IP+" -sV -PN");
			if (getSubUtil(result.get("nmap "+IP+" -sV -PN"),"Host is up")!="") {
				System.out.println(result);
				sum++;
				System.out.println("nmap获取到端口的IP个数："+sum);
			}
			
			// 释放 信号量 许可
			semaphore.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 正则匹配
	 * @param soap
	 * @param rgex
	 * @return 返回匹配的内容
	 */
 	public static String getSubUtil(String soap,String rgex){  
         Pattern pattern = Pattern.compile(rgex);// 匹配的模式  
         Matcher m = pattern.matcher(soap);  
         while(m.find()){
             return m.group(0);
         }  
         return "";  
    }
	
	public static void main(String [] args) {
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i=1;i<=254;i++) {
			list.add("172.16.0."+i);
		}
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final Semaphore semaphore = new Semaphore(5);
		for(String ip: list) {
			threadPool.execute(new NmapAllCollector(semaphore,ip,0));
		}
		
		threadPool.shutdown();
		Date nmapstarttime = new Date();
		while (true) {
			if (threadPool.isTerminated()) {
				Date nmapendtime = new Date();
 				long timetmp = nmapendtime.getTime()-nmapstarttime.getTime();
 				System.out.println("nmap获取指纹信息时间"+format.format(nmapstarttime)+"   "+format.format(nmapendtime)+"   总共用时："+timetmp+"ms");
				break;
			}
			
		}
	}

	

}
