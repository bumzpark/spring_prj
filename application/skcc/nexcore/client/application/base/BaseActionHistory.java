package skcc.nexcore.client.application.base;

public interface BaseActionHistory {

	String SUCCESS = "SUCCESS";
	String FAIL = "FAIL";

	void history(BaseAuthorityVO baseAuthority, String status, String desc);

}
