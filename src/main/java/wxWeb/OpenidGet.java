package wxWeb;

import configuration.WxConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class OpenidGet {
	private final WxApi wxApi;
	private final CloseableHttpClient client;
	private final HttpGet httpGet;
	private final String openIdUrl;
	
	public OpenidGet(WxApi wxApi) {
		this.wxApi = wxApi;
		client = HttpClients.createDefault();
		openIdUrl= WxConfiguration.getOpenIdUrl()+"?access_token="+wxApi.getAccess_token();
		httpGet=new HttpGet(openIdUrl);
	}
	
	public static void main(String[] args) throws IOException {
		HttpResponse httpResponse = HttpClients.createDefault().execute(new HttpGet(WxConfiguration.getOpenIdUrl()+"?access_token=waLbalZnAFENsr0Y1ppTUUHo7I3929rdOFrLeQhYABSq0UXe4RupBXf3jheRCXrPod0Cxk1LPEEAZ6dr4CcWBXdJcCS4iUUefteggLKmtDcJRWdACAYGL"));
		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.get("data") != null) {
				for (Object o : jsonObject.getJSONObject("data").getJSONArray("openid")) {
					System.out.println(o);
				}
			} else {
				System.out.println("///////");
			}
		}
	}
	
	public JSONArray getOpenIds(){
		try{
			HttpResponse httpResponse = client.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity);
				JSONObject jsonObject=new JSONObject(result);
				if (jsonObject.get("data")!=null) {
					return jsonObject.getJSONObject("data").getJSONArray("openid");
				}else {
					return new JSONArray();
				}
			}
			return new JSONArray();
		}catch (Exception e){
			e.printStackTrace();
			return new JSONArray();
		}
	}
}
