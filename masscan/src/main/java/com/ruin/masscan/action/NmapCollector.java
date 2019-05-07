package com.ruin.masscan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.ruin.masscan.ExecuteCmd;

public class NmapCollector implements Runnable{
	
	private Semaphore semaphore;

	private String IP;

	private String ports;
	
	List<Object[]> list =new ArrayList<Object[]>();

	
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
	
	public NmapCollector(Semaphore semaphore, String IP, String ports) {
		this.semaphore = semaphore;
		this.IP = IP;
		this.ports = ports;
	}
	
	@Override
	public void run() {
		
		try {
			// 获取 信号量 执行许可
			semaphore.acquire();
			Map<String, String> result = ExecuteCmd.execCmd("nmap -sV -sT -A -p "+ports+" "+IP);
			System.out.println(result.get("nmap -sV -sT -A -p "+ports+" "+IP));
			// 释放 信号量 许可
			semaphore.release();
//			CreateFileUtil.createTxtFile(list,"/opt/nmpMessage", "Test");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		
		ArrayList<String> list = new ArrayList<String>();
		String ipports = "172.16.0.110_649, 172.16.0.102_5900, 172.16.0.102_5901, 172.16.0.251_0, 172.16.0.102_5902, 172.16.0.102_5903, 172.16.0.102_5904, 172.16.0."+
		 "103_49153, 172.16.0.103_24007, 172.16.0.103_49152, 172.16.0.110_43840, 172.16.0.101_22, 172.16.0.101_9090, 172.16.0.103_16514, 172.16.0.251_80,"+
		  "172.16.0.100_111, 172.16.0.103_5901, 172.16.0.103_5900, 172.16.0.103_5903, 172.16.0.103_5902, 172.16.0.103_54321, 172.16.0.110_80, 172.16.0.10"+
		 "2_2223, 172.16.0.103_5905, 172.16.0.103_5904, 172.16.0.103_54322, 172.16.0.110_389, 172.16.0.100_6000, 172.16.0.100_972, 172.16.0.103_2223, 172"+
		 ".16.0.100_2049, 172.16.0.1_4786, 172.16.0.110_657, 172.16.0.110_899, 172.16.0.110_44492, 172.16.0.104_54322, 172.16.0.104_54321, 172.16.0.104_2"+
		 "2, 172.16.0.110_139, 172.16.0.253_55149, 172.16.0.100_445, 172.16.0.100_606, 172.16.0.103_9090, 172.16.0.254_443, 172.16.0.110_2049, 172.16.0.1"+
		 "10_445, 172.16.0.104_2049, 172.16.0.250_80, 172.16.0.104_16514, 172.16.0.110_609, 172.16.0.1_80, 172.16.0.110_5989, 172.16.0.101_16514, 172.16."+
		 "0.102_54321, 172.16.0.250_1194, 172.16.0.102_54322, 172.16.0.100_80, 172.16.0.104_9090, 172.16.0.102_49152, 172.16.0.104_24007, 172.16.0.102_49"+
		 "153, 172.16.0.102_49154, 172.16.0.102_22, 172.16.0.250_443, 172.16.0.110_626, 172.16.0.110_985, 172.16.0.101_24007, 172.16.0.1_22, 172.16.0.104"+
		 "_2223, 172.16.0.104_111, 172.16.0.102_9090, 172.16.0.1_23, 172.16.0.100_139, 172.16.0.101_49152, 172.16.0.110_871, 172.16.0.101_49153, 172.16.0"+
		 ".103_22, 172.16.0.250_23, 172.16.0.101_5905, 172.16.0.1_443, 172.16.0.101_5904, 172.16.0.101_5903, 172.16.0.101_5902, 172.16.0.110_853, 172.16."+
		 "0.110_851, 172.16.0.101_5906, 172.16.0.110_852, 172.16.0.253_843, 172.16.0.101_5901, 172.16.0.101_54322, 172.16.0.101_5900, 172.16.0.102_24007,"+
		  "172.16.0.101_54321, 172.16.0.110_39702, 172.16.0.100_21, 172.16.0.100_929, 172.16.0.102_16514";
		String [] ip_ports = ipports.split(",");
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final Semaphore semaphore = new Semaphore(5);
		Set<String> ipportset = new HashSet<>(Arrays.asList(ip_ports));
		for(String iString: ipportset) {
			String [] ip_prot = iString.split("_");
			threadPool.execute(new NmapCollector(semaphore,ip_prot[0],ip_prot[1]));
		}
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
