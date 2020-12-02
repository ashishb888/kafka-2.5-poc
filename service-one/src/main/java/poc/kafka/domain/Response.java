package poc.kafka.domain;

import java.util.Map;

public class Response {
	private String status;
	private Map<String, String> messages;

	public Response(String status, Map<String, String> messages) {
		this.status = status;
		this.messages = messages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

}
