package dasudian.com.im.demo;

import com.dasudian.im.sdk.MessageType;

public class DsdMessage {
	private MessageType type;
	private String content;
	private String from;
	private String groupId;
	private String time;

	public DsdMessage(MessageType type, String content, String from, String groupId) {
		super();
		this.type = type;
		this.content = content;
		this.from = from;
		this.groupId = groupId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public MessageType getType() {
		return this.type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DsdMessage [type=" + type + ", content=" + content + ", from="
				+ from + "]";
	}
}
