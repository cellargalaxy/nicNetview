package configuration;

/**
 * Created by cellargalaxy on 17-9-13.
 */
public class NetviewConfiguration {
	private static int pingTimes;
	private static int pingTimeOut;
	private static int pingThreadCount;
	private static int pingWaitTime;
	private static String ipTableName;
	
	
	
	static {
		pingTimes=4;
		pingTimeOut = 1500;
		pingThreadCount=30;
		pingWaitTime=1000*5;
		ipTableName="host";
	}
	
	public static int getPingTimes() {
		return pingTimes;
	}
	
	public static void setPingTimes(int pingTimes) {
		NetviewConfiguration.pingTimes = pingTimes;
	}
	
	public static int getPingTimeOut() {
		return pingTimeOut;
	}
	
	public static void setPingTimeOut(int pingTimeOut) {
		NetviewConfiguration.pingTimeOut = pingTimeOut;
	}
	
	public static int getPingThreadCount() {
		return pingThreadCount;
	}
	
	public static void setPingThreadCount(int pingThreadCount) {
		NetviewConfiguration.pingThreadCount = pingThreadCount;
	}
	
	public static int getPingWaitTime() {
		return pingWaitTime;
	}
	
	public static void setPingWaitTime(int pingWaitTime) {
		NetviewConfiguration.pingWaitTime = pingWaitTime;
	}
	
	public static String getIpTableName() {
		return ipTableName;
	}
	
	public static void setIpTableName(String ipTableName) {
		NetviewConfiguration.ipTableName = ipTableName;
	}
}
