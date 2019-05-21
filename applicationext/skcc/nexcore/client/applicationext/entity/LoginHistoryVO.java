package skcc.nexcore.client.applicationext.entity;

import skcc.nexcore.client.application.base.BaseVO;

public class LoginHistoryVO extends BaseVO {

	private static final long serialVersionUID = -142195020746441224L;

	public static final String STATUS_NOTFOUND = "user.notfound";
	public static final String STATUS_PASSWORD_INVALID = "password.invalid";
	public static final String STATUS_NEW_HIRE = "user.newhire";
	public static final String STATUS_INACTIVE = "user.inactive";
	public static final String STATUS_TERMINATED = "user.terminated";
	public static final String STATUS_GROUP_INVALID = "group.invalid";
	public static final String STATUS_GROUP_DISABLED = "group.disabled";
	public static final String STATUS_ACTIVE = "active";

	public String userId;
	public String loginDate;
	public String loginStatus;
	public String sessionId;
	public String clientHostAddr;
	public String groupId;

	public String userName;

}
