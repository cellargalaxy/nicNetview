package bean;

import java.util.Date;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class NetviewLog {
	private final Host host;
	private final boolean conn;
	private final Date date;
	
	public NetviewLog(Host host, boolean conn, Date date) {
		this.host = host;
		this.conn = conn;
		this.date = date;
	}
	
	public Host getHost() {
		return host;
	}
	
	public boolean isConn() {
		return conn;
	}
	
	public Date getDate() {
		return date;
	}
}
