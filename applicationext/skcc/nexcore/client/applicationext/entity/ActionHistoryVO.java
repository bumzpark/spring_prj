package skcc.nexcore.client.applicationext.entity;

import skcc.nexcore.client.application.base.BaseVO;

public class ActionHistoryVO extends BaseVO {

	private static final long serialVersionUID = -1021626371705163416L;

	//	ACTION_USER       VARCHAR2 (20)   NOT NULL,
	//	ACTION_DATE       VARCHAR2 (25)   NOT NULL,
	//	ACTION_NAME       VARCHAR2 (128)  NOT NULL,
	//	ACTION_STATUS     VARCHAR2 (20)   NOT NULL,
	//	ACTION_DESC       VARCHAR2 (1024) NOT NULL

	public String userId;
	public String sessionId;
	public String functionId;

	public String actionStatus;
	public String actionDate;
	public String actionDesc;

	public String userName;
	public String functionName;

}
