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

import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class AccessTokenGet extends FlushStatusRunnable{
	private static final Logger LOGGER = Logger.getLogger(AccessTokenGet.class.getName());
	private final String accessTokenUrl;
	private final CloseableHttpClient client;
	private final HttpGet httpGet;
	
	public AccessTokenGet() {
		accessTokenUrl= WxConfiguration.getAccessTokenUrl()+"?grant_type=client_credential&appid="+WxConfiguration.getAppID()+"&secret="+WxConfiguration.getAppsecret();
		client = HttpClients.createDefault();
		httpGet=new HttpGet(accessTokenUrl);
	}
	
	public void run() {
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jsonObject=new JSONObject(result);
				if (jsonObject.get("access_token")!=null) {
					getWxApi().setAccess_token(jsonObject.get("access_token").toString());
					LOGGER.info("成功获取access_token:"+jsonObject.get("access_token").toString());
				}else {
					LOGGER.info("失败获取access_token："+jsonObject);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("失败获取access_token");
		}
	}
}
