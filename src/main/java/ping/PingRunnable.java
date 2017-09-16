package ping;

import bean.Host;
import bean.PingResult;
import configuration.NetviewConfiguration;
import org.apache.log4j.Logger;
import service.Netview;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class PingRunnable implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(PingRunnable.class.getName());
	private Netview netview;
	private boolean runable;
	private Ping ping;
	
	public PingRunnable(Netview netview, Ping ping) {
		this.netview = netview;
		this.ping = ping;
	}
	
	public void startAble(){
		runable=true;
	}
	
	public void stopAble(){
		runable=false;
	}
	
	public void run() {
		while (runable) {
			Host[] hosts=netview.createPingHosts();
			PingResult[] pingResults=ping.pings(hosts);
			for (int i = 0; i < pingResults.length; i++) {
				netview.addPingResult(hosts[i],pingResults[i]);
			}
			try {
				Thread.sleep(NetviewConfiguration.getPingWaitTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LOGGER.info("ping完一次");
		}
	}
}
