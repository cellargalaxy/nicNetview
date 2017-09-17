package wxWeb;

/**
 * Created by cellargalaxy on 17-9-17.
 */
public abstract class FlushStatusRunnable implements Runnable{
	private WxApi wxApi;
	
	public WxApi getWxApi() {
		return wxApi;
	}
	
	public void setWxApi(WxApi wxApi) {
		this.wxApi = wxApi;
	}
}
