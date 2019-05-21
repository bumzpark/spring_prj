package skcc.nexcore.client.application.database.entity;

import skcc.nexcore.client.application.base.BaseVO;

public final class DatabaseMeta extends BaseVO {

	private static final long serialVersionUID = 2896139970671696388L;

	public String name;
	public int databaseMinorVersion;
	public int databaseMajorVersion;
	public String databaseProductName;
	public String databaseProductVersion;
	public String driverName;
	public String driverVersion;
	public String url;
	public String userName;

}
