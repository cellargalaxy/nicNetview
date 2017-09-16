package bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-13.
 * 楼栋所包含的交换机链表
 */
public class Building {
	private final String buildingName;
	private final List<Host> hosts;
	
	public Building(String buildingName) {
		this.buildingName = buildingName;
		hosts=new LinkedList<Host>();
	}
	
	public void add(Host host){
		hosts.add(host);
	}
	
	public String getBuildingName() {
		return buildingName;
	}
	
	public List<Host> getHosts() {
		return hosts;
	}
}
