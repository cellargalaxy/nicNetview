package control;

import org.json.JSONObject;
import service.Netview;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewJsonServlet extends HttpServlet {
	
	/**
	 * 添加一个Host
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String address = req.getParameter("address");
		String building = req.getParameter("building");
		String floor = req.getParameter("floor");
		String model = req.getParameter("model");
		String name = req.getParameter("name");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("address", address);
		if (address != null && building != null && floor != null && model != null) {
			Netview netview = Netview.getNETVIEW();
			try {
				jsonObject.put("result", netview.addHost(address, building, floor, model, name));
			} catch (Exception e) {
				jsonObject.put("result", false);
			}
		} else {
			jsonObject.put("result", false);
		}
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(jsonObject);
		out.close();
	}
	
	/**
	 * 删除一个Host
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String address = req.getParameter("address");
		JSONObject jsonObject = new JSONObject();
		if (address != null) {
			Netview netview = Netview.getNETVIEW();
			jsonObject.put("result", netview.deleteHost(address));
		} else {
			jsonObject.put("result", false);
		}
		jsonObject.put("address", address);
		
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(jsonObject);
		out.close();
	}
}
