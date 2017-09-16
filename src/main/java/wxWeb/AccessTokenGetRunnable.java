package wxWeb;

import configuration.WxConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import ping.PingRunnable;

import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class AccessTokenGetRunnable implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(AccessTokenGetRunnable.class.getName());
	private final WxApi wxApi;
	private final CloseableHttpClient client;
	private final HttpGet httpGet;
	private final String accessTokenUrl;
	private boolean runable;
	
	public AccessTokenGetRunnable(WxApi wxApi) {
		this.wxApi = wxApi;
		client = HttpClients.createDefault();
		accessTokenUrl= WxConfiguration.getAccessTokenUrl()+"?grant_type=client_credential&appid="+wxApi.getAppID()+"&secret="+wxApi.getAppsecret();
		httpGet=new HttpGet(accessTokenUrl);
		runable=true;
	}
	
	public void getAccessToken(){
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jsonObject=new JSONObject(result);
				if (jsonObject.get("access_token")!=null) {
					wxApi.setAccess_token(jsonObject.get("access_token").toString());
					LOGGER.info("成功获取access_token:"+jsonObject.get("access_token").toString());
				}else {
					LOGGER.info("失败获取access_token");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		runable=false;
	}
	
	public void run() {
		LOGGER.info("access_token获取线程启动");
		while (runable) {
			getAccessToken();
			try {
				Thread.sleep(WxConfiguration.getFlushAccessTokenTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
