package bean;

import java.util.Date;

/**
 * Created by cellargalaxy on 17-9-13.
 * 某次ping某个交换机的结果
 */
public class PingResult {
	private final Date date;
	private final int delay;
	
	public PingResult() {
		date = new Date();
		delay = -1;
	}
	
	public PingResult(Date date, int delay) {
		this.date = date;
		this.delay = delay;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getDelay() {
		return delay;
	}
}
