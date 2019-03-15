package com.ruin.masscan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import com.ruin.masscan.ExecuteCmd;


public class AssetCollector implements Callable<Set<String>>{

	private Semaphore semaphore;

	private ArrayList<String> IPS;
	
	private String IP;

	private String ports;
	
	private Set<String> ipports;
	
	private String filepath;
	
	private String pak;
	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public ArrayList<String> getIPS() {
		return IPS;
	}

	public void setIPS(ArrayList<String> iPS) {
		IPS = iPS;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public Set<String> getIpports() {
		return ipports;
	}

	public void setIpports(Set<String> ipports) {
		this.ipports = ipports;
	}
	
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getPak() {
		return pak;
	}

	public void setPak(String pak) {
		this.pak = pak;
	}

	public AssetCollector(Semaphore semaphore, String IP, String ports,Set<String> ipports ,String filepath,String pak) {
		this.semaphore = semaphore;
		this.IP = IP;
		this.ports = ports;
		this.ipports = ipports;
		this.filepath = filepath;
		this.pak = pak;
	}
	
	@Override
	public Set<String> call() throws Exception {
		
		// 获取 信号量 执行许可
		semaphore.acquire();
		//System.out.println("/opt/jzlog/masscan/bin/masscan "+IP+" -p"+ports);
		//String filepath = "/opt/masscan/masscan-master/bin";
		Map<String, Set<String>> result = ExecuteCmd.execCmd("./masscan "+IP+" -p"+ports+" --rate "+pak, filepath);
		System.out.println(result);
		if (!result.get("masscan"+IP).isEmpty()) {
			ipports.addAll(result.get("masscan"+IP));
		}
		// 释放 信号量 许可
		semaphore.release();
		
		return ipports;
	}
	
	public static void main(String [] args) {
		// 参数开始IP
		String startIP = "192.168.0.1";
		// 参数结束IP
		String endIP = "192.168.0.1";
		// 参数发包速率
		String pak = "200";
		// masscan 所在路径
		String filepath = "/opt/jzlog/masscan/bin";
		
		if(args.length<1) {
			System.out.println("请输入起始IP地址和终止IP地址！");
		}else if (args.length>=1&&args.length<2) {
			startIP = args[0];
		}else if (args.length==2) {
			startIP = args[0];
			endIP = args[1];
		}else if (args.length==3) {
			startIP = args[0];
			endIP = args[1];
			pak = args[2];
		}else if (args.length==4) {
			startIP = args[0];
			endIP = args[1];
			pak = args[2];
			filepath = args[3];
		}else {
			System.out.println("参数错误");
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		final Semaphore semaphore = new Semaphore(5);

		String ports1 = "1,11,13,15,17,19,21,22,23,25,26,30,31,32,33,34,35,36,37,38,39,43,53,69,70,79,80,81,82,83,84,85,88,98,100,102,110,111,113,119,123,135,137,139,143,161,179,199,214,264,280,322,389,407,443,444,445,449,465,497,500,502,505,510,514,515,517,518,523,540,548,587,591,616,620,623,626,628,631,636,666,731,771,782,783,789,873,888,898,900,901,902,989,990,992,993,994,995,1000,1001,1010,1022,1023,1026,1040,1041,1042,1043,1091,1098,1099,1200,1212,1214,1220,1234,1241,1248,1302,1311,1314,1344,1400,1419,1432,1433,1434,1443,1467,1471,1501,1503,1505,1521,1604,1610,1611,1666,1687,1688,1720,1723,1830,1901,1911,1947,1962,1967,2000,2001,2002,2010,2024,2030,2048,2051,2052,2055,2064,2080,2082,2083,2086,2087,2160,2181,2222,2252,2306,2323,2332,2375,2376,2396,2404,2406,2427,2443,2455,2480,2525,2600,2628,2715,2869,2967,3000,3002,3005,3052,3075,3128,3280,3306,3310,3333,3372,3388,3389,3443,3478,3531,3689,3774,3790,3872,3940,4000,4022,4040,4045,4155,4300,4369,4433,4443,4444,4567,4660,4711,4848,4911,5000,5001,5007,5009,5038,5050,5051,5060,5061,5222,5269,5280,5357,5400,5427,5432,5443,5550,5555,5560,5570,5598,5601,5632,5800,5801,5802,5803,5820,5900,5901,5902,5984,5985,5986,6000,6060,6061,6080,6103,6112,6346,6379,6432,6443,6544,6600,6666,6667,6668,6669,6670,6679,6697,6699,6779,6780,6782,6969,7000,7001,7002,7007,7070,7077,7100,7144,7145,7180,7187,7199,7200,7210,7272,7402,7443,7479,7776,7777,7780,8000,8001,8002,8003,8004,8005,8006,8007,8008,8009,8010,8025,8030,8042,8060,8069,8080,8083,8084,8085,8086,8087,8088,8089,8090,8098,8112,8118,8129,8138,8181,8182,8194,8333,8351,8443,8480,8500,8529,8554,8649,8765,8834,8880,8881,8882,8883,8884,8885,8886,8887,8888,8890,8899,8983,9000,9001,9002,9003,9030,9050,9051,9080,9083,9090,9091,9100,9151,9191,9200,9292,9300,9333,9334,9443,9527,9595,9600,9801,9864,9870,9876,9943,9944,9981,9997,9999,10000,10001,10005,10030,10035,10080,10243,10443,11000,11211,11371,11965,12000,12203,12345,12999,13013,13666,13720,13722,14000,14443,14534,15000,15001,15002,16000,16010,16922,16923,16992,16993,17988,18080,18086,18264,19150,19888,19999,20000,20547,23023,25000,25010,25020,25565,26214,26470,27015,27017,27960,28006,28017,29999,30444,31337,31416,32400,32750,32751,32752,32753,32754,32755,32756,32757,32758,32759,32760,32761,32762,32763,32764,32765,32766,32767,32768,32769,32770,32771,32772,32773,32774,32775,32776,32777,32778,32779,32780,32781,32782,32783,32784,32785,32786,32787,32788,32789,32790,32791,32792,32793,32794,32795,32796,32797,32798,32799,32800,32801,32802,32803,32804,32805,32806,32807,32808,32809,32810,34012,34567,34599,37215,37777,38978,40000,40001,40193,44443,44818,47808,49152,49153,50000,50030,50060,50070,50075,50090,50095,50100,50111,50200,52869,53413,55555,56667,60010,60030,60443,61616,64210,64738";
		String ports2 = "0-65535";
		
		String [] ports = {ports2};
		
 		String [] startips = startIP.split("\\.");
 		String [] endips = endIP.split("\\.");
 		ArrayList<String> list = new ArrayList<String>();
 		for(int i = Integer.valueOf(startips[3]);i<=Integer.valueOf(endips[3]);i++ ) {
 			list.add(startips[0]+"."+startips[1]+"."+startips[2]+"."+i);
 		}
 		System.out.println(list);
		
 		Date starttime = new Date();
 		
 		Set<String> ipports = new HashSet<>();
 		Future<Set<String>> future = null;
 		for(String port:ports){
 			for(String ip : list) {
 	 			future = threadPool.submit(new AssetCollector(semaphore, ip, port,ipports,filepath,pak));
 	 		}
 		}
 		
 		
 		threadPool.shutdown();
 		
		while (true) {
			
 			if(future.isDone()&&threadPool.isTerminated()){
 				Date endtime = new Date();
				System.out.println("-----------------------");
				System.out.println(ipports.size()+"  "+ipports);
				long timetmp = endtime.getTime()-starttime.getTime();
				System.out.println("masscan 开始时间："+format.format(starttime)+", masscan 结束时间："+format.format(endtime)+"   总共用时："+timetmp+"ms");
				/*for(String ipport : ipports) {
					String [] ip_prot = ipport.split("_");
					ExecuteCmd.execCmd("nmap -p "+ip_prot[1]+" "+ip_prot[0]);
				}*/
				break;
			}
			
		}
		threadPool.shutdownNow();
		Date nmapstarttime = new Date();
		for(String ipport : ipports) {
			String [] ip_prot = ipport.split("_");
			ExecuteCmd.execCmd("nmap -sV -sT -A -p "+ip_prot[1]+" "+ip_prot[0]);
		}
		Date nmapendtime = new Date();
		System.out.println("nmap获取指纹信息时间"+format.format(nmapstarttime)+"   "+format.format(nmapendtime));
	}

}
