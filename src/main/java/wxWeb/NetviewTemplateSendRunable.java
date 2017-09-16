package wxWeb;

import bean.NetviewLog;
import configuration.WxConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ping.PingRunnable;
import service.Netview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewTemplateSendRunable implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(NetviewTemplateSendRunable.class.getName());
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss");
	private final WxApi wxApi;
	private final CloseableHttpClient client;
	private final HttpPost post;
	private final String template_id;
	private final String url;
	private final OpenidGet openidGet;
	private boolean runable;
	
	public NetviewTemplateSendRunable(WxApi wxApi, String template_id, String url) {
		this.wxApi = wxApi;
		this.template_id = template_id;
		this.url = url;
		client = HttpClients.createDefault();
		post = new HttpPost(WxConfiguration.getTemplateUrl()+"?access_token="+wxApi.getAccess_token());
		openidGet=new OpenidGet(wxApi);
		runable=true;
	}
	
	public void stop(){
		runable=false;
	}
	
	public void run() {
		LOGGER.info("发送模板线程启动");
		while (runable) {
			try{
				List<NetviewLog> netviewLogs= Netview.getNETVIEW().getNetviewLogs();
				if (netviewLogs.size()==0) {
					continue;
				}
				StringBuffer stringBuffer=new StringBuffer();
				synchronized (netviewLogs){
					for (NetviewLog netviewLog : netviewLogs) {
						if (netviewLog.isConn()) {
							stringBuffer.append("T " + DATE_FORMAT.format(netviewLog.getDate())+" "+netviewLog.getHost().getFullName()+"\n");
						}else {
							stringBuffer.append("F " + DATE_FORMAT.format(netviewLog.getDate())+" "+netviewLog.getHost().getFullName()+"\n");
						}
					}
				}
				JSONArray openIds=openidGet.getOpenIds();
				for (Object openId : openIds) {
					JSONObject info=new JSONObject();
					info.put("value",stringBuffer.toString());
					JSONObject data=new JSONObject();
					data.put("info",info);
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("touser",openId);
					jsonObject.put("template_id",template_id);
					jsonObject.put("url",url);
					jsonObject.put("data",data);
					StringEntity stringEntity = new StringEntity(jsonObject.toString());
					stringEntity.setContentEncoding("UTF-8");
					stringEntity.setContentType("application/json");//发送json数据需要设置contentType
					post.setEntity(stringEntity);
					HttpResponse httpResponse = client.execute(post);
					if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						HttpEntity entity = httpResponse.getEntity();
						String result = EntityUtils.toString(entity);
						JSONObject response = new JSONObject(result);
						LOGGER.info("成功发送模板");
					}else {
						LOGGER.info("失败发送模板");
					}
				}
				Netview.getNETVIEW().clearNetviewLogs();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
