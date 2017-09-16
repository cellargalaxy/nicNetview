package ping;


import configuration.NetviewConfiguration;
import bean.Host;
import bean.PingResult;
import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cellargalaxy on 17-9-13.
 * http://commons.apache.org/proper/commons-exec/tutorial.html
 */
public class PingImpl implements Ping{
	private static final String LINUX_PING_COMMAND="ping -c 1 ";
	private static final String WINDOWS_PING_COMMAND="ping -n 1 ";
	private static final Pattern PATTERN = Pattern.compile("\\s*\\d*\\.*\\d+\\s*ms");
	
	public PingResult[] pings(Host[] hosts) {
		ByteArrayOutputStream[] byteArrayOutputStreams=new ByteArrayOutputStream[hosts.length];
		for (int i = 0; i < byteArrayOutputStreams.length; i++) {
			byteArrayOutputStreams[i]=ping(hosts[i].getAddress());
			if (i% NetviewConfiguration.getPingThreadCount()==0) {
				try {
					Thread.sleep(NetviewConfiguration.getPingTimeOut());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(NetviewConfiguration.getPingTimeOut());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dealPingByteArrayOutputStreams(byteArrayOutputStreams);
	}
	
	public PingResult[] pings(List<Host> hosts) {
		ByteArrayOutputStream[] byteArrayOutputStreams=new ByteArrayOutputStream[hosts.size()];
		int i=0;
		for (Host host : hosts) {
			byteArrayOutputStreams[i]=ping(host.getAddress());
			if (i% NetviewConfiguration.getPingThreadCount()==0) {
				try {
					Thread.sleep(NetviewConfiguration.getPingTimeOut());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			i++;
		}
		try {
			Thread.sleep(NetviewConfiguration.getPingTimeOut());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dealPingByteArrayOutputStreams(byteArrayOutputStreams);
	}
	
	private ByteArrayOutputStream ping(String address) {
		try {
			DefaultExecutor executor = new DaemonExecutor();
			ExecuteWatchdog watchdog = new ExecuteWatchdog(NetviewConfiguration.getPingTimeOut());
			executor.setWatchdog(watchdog);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PumpStreamHandler pumpStreamHandler=new PumpStreamHandler(byteArrayOutputStream, byteArrayOutputStream);
			executor.setStreamHandler(pumpStreamHandler);
			CommandLine commandLine;
			if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
				commandLine = CommandLine.parse(WINDOWS_PING_COMMAND+address);
			}else {
				commandLine = CommandLine.parse(LINUX_PING_COMMAND+address);
			}
			executor.execute(commandLine);
			return byteArrayOutputStream;
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	private PingResult[] dealPingByteArrayOutputStreams(ByteArrayOutputStream[] byteArrayOutputStreams){
		PingResult[] pingResults=new PingResult[byteArrayOutputStreams.length];
		for (int i = 0; i < byteArrayOutputStreams.length; i++) {
			int delay;
			if (byteArrayOutputStreams[i]==null) {
				delay=-1;
			}else {
				Matcher matcher = PATTERN.matcher(byteArrayOutputStreams[i].toString());
				if (matcher.find()) {
					String delayString = matcher.group();
					double d=new Double(delayString.substring(0, delayString.length() - 2));
					delay=(int)d;
				} else {
					delay=-1;
				}
			}
			pingResults[i]=new PingResult(new Date(),delay);
		}
		return pingResults;
	}
}
