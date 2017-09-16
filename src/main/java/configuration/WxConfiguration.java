package configuration;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class WxConfiguration {
	private static String appID;
	private static String appsecret;
	private static String accessTokenUrl;
	private static int flushAccessTokenTime;
	private static String templateUrl;
	private static String openIdUrl;
	
	static {
		appID="";
		appsecret="";
		accessTokenUrl="https://api.weixin.qq.com/cgi-bin/token";
		flushAccessTokenTime=1000*60*60;
		templateUrl="https://api.weixin.qq.com/cgi-bin/message/template/send";
		openIdUrl="https://api.weixin.qq.com/cgi-bin/user/get";
	}
	
	public static String getAppID() {
		return appID;
	}
	
	public static void setAppID(String appID) {
		WxConfiguration.appID = appID;
	}
	
	public static String getAppsecret() {
		return appsecret;
	}
	
	public static void setAppsecret(String appsecret) {
		WxConfiguration.appsecret = appsecret;
	}
	
	public static String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	
	public static void setAccessTokenUrl(String accessTokenUrl) {
		WxConfiguration.accessTokenUrl = accessTokenUrl;
	}
	
	public static int getFlushAccessTokenTime() {
		return flushAccessTokenTime;
	}
	
	public static void setFlushAccessTokenTime(int flushAccessTokenTime) {
		WxConfiguration.flushAccessTokenTime = flushAccessTokenTime;
	}
	
	public static String getTemplateUrl() {
		return templateUrl;
	}
	
	public static void setTemplateUrl(String templateUrl) {
		WxConfiguration.templateUrl = templateUrl;
	}
	
	public static String getOpenIdUrl() {
		return openIdUrl;
	}
	
	public static void setOpenIdUrl(String openIdUrl) {
		WxConfiguration.openIdUrl = openIdUrl;
	}
}
