package wxWeb;

import configuration.WxConfiguration;
import org.apache.log4j.Logger;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class WxApi {
	private static final Logger LOGGER = Logger.getLogger(NetviewTemplateSendRunable.class.getName());
	private final String appID= WxConfiguration.getAppID();
	private final String appsecret=WxConfiguration.getAppsecret();
	private String access_token;
	
	private AccessTokenGetRunnable accessTokenGetRunnable;
	private NetviewTemplateSendRunable netviewTemplateSendRunable;
	
	private final static WxApi WX_API=new WxApi();
	
	public static WxApi getWxApi() {
		return WX_API;
	}
	
	public static void main(String[] args) {
		WxApi.getWxApi().start();
	}
	
	private WxApi() {
	}
	
	public void start(){
		accessTokenGetRunnable =new AccessTokenGetRunnable(this);
		accessTokenGetRunnable.getAccessToken();
		netviewTemplateSendRunable=new NetviewTemplateSendRunable(this,"BxdhCUvfr2m7OTwws6wmh75p1REXTTwyLqo0L1_GS10","http://119.29.171.44:8080/nic/");
		new Thread(accessTokenGetRunnable,"获取access_token线程").start();
		new Thread(netviewTemplateSendRunable,"发送模板线程").start();
	}
	
	public void stop(){
		accessTokenGetRunnable.stop();
		netviewTemplateSendRunable.stop();
	}
	
	public String getAppID() {
		return appID;
	}
	
	public String getAppsecret() {
		return appsecret;
	}
	
	public String getAccess_token() {
		synchronized (access_token){
			return access_token;
		}
	}
	
	public void setAccess_token(String access_token) {
		synchronized (access_token){
			this.access_token = access_token;
		}
	}
}
