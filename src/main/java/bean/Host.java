package bean;

import configuration.NetviewConfiguration;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-13.
 * 交换机对象
 */
public class Host {
	private static final int pingTimes = NetviewConfiguration.getPingTimes();
	private String address;
	private String building;
	private String floor;
	private String model;
	private String name;
	private final LinkedList<PingResult> results;
	private boolean conn;
	private Date date;
	
	public Host(String address, String building, String floor, String model, String name) {
		this.address = address;
		this.building = building;
		this.floor = floor;
		this.model = model;
		this.name = name;
		results = new LinkedList<PingResult>();
		for (int i = 0; i < pingTimes; i++) {
			results.add(new PingResult());
		}
		conn = true;
		date = new Date();
	}
	
	public Host() {
		results = new LinkedList<PingResult>();
		for (int i = 0; i < pingTimes; i++) {
			results.add(new PingResult());
		}
		conn = true;
		date = new Date();
	}
	
	/**
	 * 如果返回true，则连通状态有改变，并且修改时间，否则没
	 *
	 * @param result
	 * @return
	 */
	public boolean addResult(PingResult result) {
		results.poll();
		results.add(result);
		if (conn) {
			for (PingResult pingResult : results) {
				if (pingResult.getDelay() >= 0) {
					return false;
				}
			}
			conn = false;
			date=result.getDate();
			return true;
		} else {
			if (result.getDelay() >= 0) {
				conn = true;
				date=result.getDate();
				return true;
			} else {
				return false;
			}
		}
	}
	
	public String getFullName() {
		if (name != null&&name.length()>0) {
			return building + "-" + floor + "-" + model + "-" + name;
		} else {
			return building + "-" + floor + "-" + model;
		}
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getFloor() {
		return floor;
	}
	
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LinkedList<PingResult> getResults() {
		return results;
	}
	
	public boolean isConn() {
		return conn;
	}
	
	public void setConn(boolean conn) {
		this.conn = conn;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Host{" +
				"address='" + address + '\'' +
				", building='" + building + '\'' +
				", floor='" + floor + '\'' +
				", model='" + model + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
