package skcc.nexcore.client.application.util;

public class Data implements java.io.Serializable {

	private static final long serialVersionUID = 7590360776895992965L;

	public String group;
	public String id;
	public String value;

	public Data(String group, String id, String value) {
		this.group = group;
		this.id = id;
		this.value = value;
	}

}
