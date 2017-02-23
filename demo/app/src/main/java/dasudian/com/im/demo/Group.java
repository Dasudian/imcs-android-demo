package dasudian.com.im.demo;

public class Group {
	private String groupId;
	private String name;

	public Group(String groupId, String name) {
		super();
		this.groupId = groupId;
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
