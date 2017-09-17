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
import service.NetviewThread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewTemplateSendThread extends Thread{
	private static final Logger LOGGER = Logger.getLogger(NetviewTemplateSendThread.class.getName());
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss");
	private final WxApi wxApi;
	private final String template_id;
	private String templateUrl;
	private JSONArray openIds;
	private final String url;
	private final CloseableHttpClient client;
	private HttpPost post;
	private boolean runable;
	
	public NetviewTemplateSendThread(WxApi wxApi, String template_id, JSONArray openIds, String url) {
		this.wxApi = wxApi;
		this.template_id = template_id;
		this.openIds = openIds;
		this.url=url;
		client = HttpClients.createDefault();
		runable=true;
	}
	
	@Override
	public void interrupt() {
		runable=false;
		super.interrupt();
	}
	
	private void sendNetviewTemplate(){
		try{
			List<NetviewLog> netviewLogs= NetviewThread.getNETVIEW().getNetviewLogs();
			if (netviewLogs.size()==0) {
				return;
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
				NetviewThread.getNETVIEW().clearNetviewLogs();
			}
			templateUrl=WxConfiguration.getTemplateUrl()+"?access_token="+wxApi.getAccess_token();
			post = new HttpPost(templateUrl);
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
				stringEntity.setContentEncoding("utf-8");
				stringEntity.setContentType("application/json");
				post.setEntity(stringEntity);
				HttpResponse httpResponse = client.execute(post);
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					HttpEntity entity = httpResponse.getEntity();
					String result = EntityUtils.toString(entity);
					JSONObject response = new JSONObject(result);
					if (response.get("errmsg").toString().equals("ok")) {
						LOGGER.info("成功发送监控模板");
					}else {
						LOGGER.info("失败发送监控模板："+response);
					}
					
				}else {
					LOGGER.info("失败发送监控模板："+httpResponse.getStatusLine().getStatusCode());
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.info("失败发送监控模板");
		}
	}
	
	@Override
	public void run() {
		LOGGER.info("发送监控模板线程启动");
		while (runable) {
			sendNetviewTemplate();
			try {
				Thread.sleep(WxConfiguration.getTemplateSendTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
