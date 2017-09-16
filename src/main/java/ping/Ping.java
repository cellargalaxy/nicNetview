package ping;

import bean.Host;
import bean.PingResult;

import java.util.List;

/**
 * Created by cellargalaxy on 17-9-13.
 */
public interface Ping {
	PingResult[] pings(Host[] hosts);
	PingResult[] pings(List<Host> hosts);
}
