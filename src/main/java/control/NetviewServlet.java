package control;

import bean.Building;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import service.Netview;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewServlet extends HttpServlet {
	private static final String NETVIEW_JSP = "/WEB-INF/jsp/netview.jsp";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Netview netview = Netview.getNETVIEW();
		String demandKey = req.getParameter("demandKey");
		if (demandKey == null) {
			LinkedList<Building> buildings = netview.createAllBuilding();
			buildings.addFirst(netview.createOutTimeBuilding());
			req.setAttribute("buildings", buildings);
		} else {
			req.setAttribute("buildings", new Building[]{netview.createDemandKeyBuilding(demandKey)});
		}
		req.getRequestDispatcher(NETVIEW_JSP).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获得磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获取文件需要上传到的路径
		String path=getServletContext().getRealPath("/WEB-INF/upload");
		//设置存储室
		factory.setRepository(new File(path));
		//设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);
		//文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		File ipFile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			//可以上传多个文件
			List<FileItem> list = upload.parseRequest(req);
			for (FileItem item : list) {
				if (!item.isFormField()) {
					//获取路径名
					String filename = item.getName();
					//索引到最后一个反斜杠
					int start = filename.lastIndexOf("\\");
					//截取 上传文件的 字符串名字，加1是 去掉反斜杠
					filename = path + "/" + filename.substring(start + 1);
					ipFile = new File(filename);
					ipFile.getParentFile().mkdirs();
					inputStream = item.getInputStream();
					outputStream = new BufferedOutputStream(new FileOutputStream(ipFile));
					int len;
					byte[] bytes = new byte[1024];
					while ((len = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, len);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Netview netview = Netview.getNETVIEW();
		req.setAttribute("buildings", new Building[]{netview.addHosts(ipFile)});
		ipFile.delete();
		req.getRequestDispatcher(NETVIEW_JSP).forward(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String address = req.getParameter("address");
		String building = req.getParameter("building");
		String floor = req.getParameter("floor");
		String model = req.getParameter("model");
		String name = req.getParameter("name");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("address", address);
		System.out.println(address+building+floor+model+name);
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
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
