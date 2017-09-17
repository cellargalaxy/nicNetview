package service;

import bean.Building;
import bean.Host;
import bean.NetviewLog;
import bean.PingResult;
import configuration.NetviewConfiguration;
import dao.Sql;
import org.apache.log4j.Logger;
import ping.Ping;
import ping.PingImpl;

import java.io.File;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewThread extends Thread{
	private static final Logger LOGGER = Logger.getLogger(NetviewThread.class.getName());
	private final Ping ping;
	private boolean runable;
	private final List<Host> hosts;
	private final ExcelReadHost excelReadHost;
	private final List<NetviewLog> netviewLogs;
	
	private static final NetviewThread NETVIEW_THREAD =new NetviewThread(new PingImpl(),new ExcelReadHostImpl());
	public static NetviewThread getNETVIEW() {
		return NETVIEW_THREAD;
	}
	
	
	private NetviewThread(Ping ping, ExcelReadHost excelReadHost) {
		this.ping=ping;
		this.excelReadHost=excelReadHost;
		runable=true;
		hosts= Sql.findAllHosts();
		sortHost();
		netviewLogs=new LinkedList<NetviewLog>();
		LOGGER.info("初始化监控主类");
	}
	
	private void sortHost() {
		Map<String, Host> map = new TreeMap<String, Host>();
		for (Host host : hosts) {
			map.put(host.getBuilding() + host.getFloor() + host.getAddress(), host);
		}
		hosts.clear();
		for (Map.Entry<String, Host> entry : map.entrySet()) {
			hosts.add(entry.getValue());
		}
	}
	
	@Override
	public void interrupt() {
		runable=false;
		super.interrupt();
	}
	
	@Override
	public void run() {
		while (runable) {
			Host[] hosts= createPingHosts();
			PingResult[] pingResults=ping.pings(hosts);
			for (int i = 0; i < pingResults.length; i++) {
				addPingResult(hosts[i],pingResults[i]);
			}
			LOGGER.info("ping完一次");
			try {
				Thread.sleep(NetviewConfiguration.getPingWaitTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean addHost(String address, String building, String floor, String model, String name) {
		Host host = new Host(address, building, floor, model, name);
		return addHost(host);
	}
	
	private boolean addHost(Host host) {
		if (Sql.addAddress(host)) {
			synchronized (hosts){
				hosts.add(host);
			}
			LOGGER.info("添加一个主机; address:" + host.getAddress() + " 地址:" + host.getFullName());
			return true;
		} else {
			return false;
		}
	}
	
	public Building addHosts(File file) {
		List<Host> hosts = excelReadHost.readExcelHost(file);
		if (hosts == null) {
			return new Building("文件格式异常，添加失败！");
		} else {
			Building building = new Building("下列Host添加失败");
			synchronized (hosts) {
				for (Host host : hosts) {
					if (!addHost(host)) {
						building.add(host);
					}
				}
			}
			return building;
		}
	}
	
	public boolean deleteHost(String address) {
		if (Sql.delleteAddress(address)) {
			synchronized (hosts){
				Iterator<Host> iterator = hosts.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getAddress().equals(address)) {
						iterator.remove();
						LOGGER.info("删除主机：" + address);
						return true;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public Building createOutTimeBuilding() {
		Building building = new Building("超时");
		synchronized (hosts){
			for (Host host : hosts) {
				if (!host.isConn()) {
					building.add(host);
				}
			}
		}
		return building;
	}
	
	public Building createDemandKeyBuilding(String demandKey) {
		Building building = new Building("查询：" + demandKey);
		synchronized (hosts){
			for (Host host : hosts) {
				if (host.getAddress().contains(demandKey) || host.getFullName().contains(demandKey) ||
						demandKey.contains(host.getAddress()) || demandKey.contains(host.getFullName())) {
					building.add(host);
				}
			}
		}
		return building;
	}
	
	
	public LinkedList<Building> createAllBuilding() {
		LinkedList<Building> buildings = new LinkedList<Building>();
		synchronized (hosts){
			main:
			for (Host host : hosts) {
				for (Building building : buildings) {
					if (building.getBuildingName().equals(host.getBuilding().toUpperCase())) {
						building.add(host);
						continue main;
					}
				}
				Building building = new Building(host.getBuilding().toUpperCase());
				building.add(host);
				buildings.add(building);
			}
		}
		return buildings;
	}
	
	public Host[] createPingHosts(){
		Host[] pingHosts=new Host[hosts.size()];
		int i=0;
		for (Host host : hosts) {
			pingHosts[i]=host;
			i++;
		}
		return pingHosts;
	}
	
	public void addPingResult(Host host, PingResult pingResult){
		if (host.addResult(pingResult)) {
			synchronized (netviewLogs){
				netviewLogs.add(new NetviewLog(host,host.isConn(),pingResult.getDate()));
			}
			LOGGER.info("交换机状态改变："+host.isConn()+" "+host.getFullName());
		}
	}
	
	public List<NetviewLog> getNetviewLogs() {
		synchronized (netviewLogs){
			return netviewLogs;
		}
	}
	
	public void clearNetviewLogs() {
		synchronized (netviewLogs){
			netviewLogs.clear();
		}
	}
}
