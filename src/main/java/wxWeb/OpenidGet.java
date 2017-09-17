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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class OpenidGet extends FlushStatusRunnable{
	private static final Logger LOGGER = Logger.getLogger(OpenidGet.class.getName());
	private String openIdUrl;
	private final CloseableHttpClient client;
	private HttpGet httpGet;
	
	public OpenidGet() {
		client = HttpClients.createDefault();
	}
	
	public void run() {
		try{
			openIdUrl= WxConfiguration.getOpenIdUrl()+"?access_token="+getWxApi().getAccess_token();
			httpGet=new HttpGet(openIdUrl);
			HttpResponse httpResponse = client.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jsonObject=new JSONObject(result);
				if (jsonObject.get("data")!=null) {
					getWxApi().setOpenIds(jsonObject.getJSONObject("data").getJSONArray("openid"));
					LOGGER.info("成功获取openId，关注人数："+jsonObject.get("total"));
				}else {
					LOGGER.info("失败获取openId："+jsonObject);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.info("失败获取openId");
		}
	}
}
