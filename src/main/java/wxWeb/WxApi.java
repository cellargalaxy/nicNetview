package wxWeb;

import configuration.WxConfiguration;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.util.concurrent.CountDownLatch;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class WxApi extends Thread{
	private static final Logger LOGGER = Logger.getLogger(WxApi.class.getName());
	private final CountDownLatch countDownLatch;
	private final FlushStatusRunnable[] flushStatusRunnables;
	private volatile String access_token;
	private volatile JSONArray openIds;
	
	private boolean runable;
	
	private static WxApi WX_API;
	public static WxApi createWxApi(FlushStatusRunnable[] flushStatusRunnables) {
		if (WX_API==null||flushStatusRunnables!=null) {
			WX_API=new WxApi(flushStatusRunnables);
		}
		return WX_API;
	}
	private WxApi(FlushStatusRunnable[] flushStatusRunnables) {
		this.flushStatusRunnables = flushStatusRunnables;
		countDownLatch=new CountDownLatch(1);
		runable=true;
		LOGGER.info("初始化微信公众号主类");
	}
	
	@Override
	public void interrupt() {
		runable=false;
		super.interrupt();
	}
	
	@Override
	public void run() {
		for (FlushStatusRunnable flushStatusRunnable : flushStatusRunnables) {
			flushStatusRunnable.setWxApi(this);
		}
		for (FlushStatusRunnable flushStatusRunnable : flushStatusRunnables) {
			flushStatusRunnable.run();
		}
		countDownLatch.countDown();
		while (runable) {
			try {
				Thread.sleep(WxConfiguration.getFlushTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (FlushStatusRunnable flushStatusRunnable : flushStatusRunnables) {
				flushStatusRunnable.run();
			}
		}
	}
	
	public String getAccess_token() {
		return access_token;
	}
	
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public JSONArray getOpenIds() {
		return openIds;
	}
	
	public void setOpenIds(JSONArray openIds) {
		this.openIds = openIds;
	}
	
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
}
