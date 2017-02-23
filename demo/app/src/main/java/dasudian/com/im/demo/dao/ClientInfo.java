package dasudian.com.im.demo.dao;

public class ClientInfo {
	private String userId;
	private String clientId;

	public ClientInfo(String userId, String clientId) {
		this.userId = userId;
		this.clientId = clientId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
