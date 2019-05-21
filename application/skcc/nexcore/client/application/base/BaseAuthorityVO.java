package skcc.nexcore.client.application.base;

public class BaseAuthorityVO extends BaseVO {

	private static final long serialVersionUID = 5070092238701170481L;

	public String userId;
	public String sessionId;
	public String groupId;
	public String functionId;
	public boolean selectEnabled;
	public boolean insertEnabled;
	public boolean updateEnabled;
	public boolean deleteEnabled;
	public boolean serverEnabled;

}
