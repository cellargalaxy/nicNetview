package control;

import service.NetviewThread;
import wxWeb.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewListener implements ServletContextListener {
	private NetviewTemplateSendThread netviewTemplateSendThread;
	
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		NetviewThread netviewThread=NetviewThread.getNETVIEW();
		netviewThread.setName("交换机监控线程");
		netviewThread.start();
		FlushStatusRunnable[] flushStatusRunnables={new AccessTokenGet(),new OpenidGet()};
		WxApi wxApi=WxApi.createWxApi(flushStatusRunnables);
		wxApi.setName("微信公众号状态更新线程");
		wxApi.start();
		try {
			wxApi.getCountDownLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		netviewTemplateSendThread =new NetviewTemplateSendThread(wxApi,"BxdhCUvfr2m7OTwws6wmh75p1REXTTwyLqo0L1_GS10",wxApi.getOpenIds(),"http://119.29.171.44:8080/nic");
		netviewTemplateSendThread.setName("微信模板发送线程");
		netviewTemplateSendThread.start();
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		NetviewThread netviewThread=NetviewThread.getNETVIEW();
		netviewThread.interrupt();
		WxApi.createWxApi(null).interrupt();
		netviewTemplateSendThread.interrupt();
	}
}
