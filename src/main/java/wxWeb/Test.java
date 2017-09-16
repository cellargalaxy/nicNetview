package wxWeb;


import com.sun.deploy.net.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-15.
 */
public class Test extends HttpServlet {
	private static String access_token="bhYZrMdDQbcuxTsS_mM-iAJzfEUEmRy49q-Ir3p8PeSmU0GCunE111lUCZZhzldntz-acBF6GkJI3j9tDiu9_nVexah0T9GXoz6wgTRI4cFKiUjcmyU875U-W3L-w2QEBJMhAEAJRU";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String signature = req.getParameter("signature");
		System.out.println("signature: "+signature);
		// 时间戳
		String timestamp = req.getParameter("timestamp");
		System.out.println("timestamp: "+timestamp);
		// 随机数
		String nonce = req.getParameter("nonce");
		System.out.println("nonce: "+nonce);
		// 随机字符串
		String echostr = req.getParameter("echostr");
		System.out.println("echostr: "+echostr);
		
		PrintWriter printWriter=resp.getWriter();
		printWriter.write(echostr);
		printWriter.close();
	}
	
	public static void main(String[] args) throws IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
		JSONObject kw1=new JSONObject();
		kw1.put("value","aaaaaaaa");
		JSONObject kw2=new JSONObject();
		kw2.put("value","bbbbbb");
		JSONObject data=new JSONObject();
		data.put("kw1",kw1);
		data.put("kw2",kw2);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("touser","oOl0Q08W7jUEB2i0BCfp-T3tbE90");
		jsonObject.put("template_id","GBkahqcPh9wUGlQLfuyRwjv3JPCpF_cyOdwiQMIq3bo");
		jsonObject.put("url","http://weixin.qq.com/download");
		jsonObject.put("data",data);
		String ret = doPost(url, jsonObject).toString();
		System.out.println(ret);
		
	}
	
	public static JSONObject doPost(String url,JSONObject json){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			System.out.println(json.toString());
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");//发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				String result = EntityUtils.toString(entity);// 返回json格式：
				response = new JSONObject(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}
}


