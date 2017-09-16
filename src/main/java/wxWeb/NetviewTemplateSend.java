package wxWeb;

import bean.Host;
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
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewTemplateSend{
	
	
//	public static void main(String[] args) throws InterruptedException {
//		WxApi wxApi=new WxApi();
//		AccessTokenGetRunnable accessTokenGetRunnable =new AccessTokenGetRunnable(wxApi);
//		new Thread(accessTokenGetRunnable).start();
//		Thread.sleep(1000*5);
//		NetviewTemplateSend netviewTemplateSend=new NetviewTemplateSend(wxApi,"BxdhCUvfr2m7OTwws6wmh75p1REXTTwyLqo0L1_GS10","http://119.29.171.44:8080/nic/");
//		List<NetviewLog> netviewLogs=new LinkedList<NetviewLog>();
//		Host host=new Host("127.0.0.1","A1","1F","S1234","1");
//		netviewLogs.add(new NetviewLog(host,false,new Date()));
//		netviewTemplateSend.setNetviewLogs(netviewLogs);
//		System.out.println(netviewTemplateSend.sendTemplate("oOl0Q08W7jUEB2i0BCfp-T3tbE90"));
//	}
	
	
}
