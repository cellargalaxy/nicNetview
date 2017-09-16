package control;

import service.Netview;
import wxWeb.WxApi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		Netview.getNETVIEW().startNetview();
		WxApi.getWxApi().start();
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		Netview.getNETVIEW().stopNetview();
		WxApi.getWxApi().stop();
	}
}
