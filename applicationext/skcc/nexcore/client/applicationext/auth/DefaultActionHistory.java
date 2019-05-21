package skcc.nexcore.client.applicationext.auth;

import java.sql.Timestamp;

import skcc.nexcore.client.application.base.BaseActionHistory;
import skcc.nexcore.client.application.base.BaseAuthorityVO;
import skcc.nexcore.client.applicationext.entity.ActionHistoryVO;
import skcc.nexcore.client.applicationext.service.ActionHistoryService;
import skcc.nexcore.client.foundation.util.DateTimeUtils;

public class DefaultActionHistory implements BaseActionHistory {

	private ActionHistoryService actionHistoryService;

	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}

	public void history(BaseAuthorityVO baseAuthority, String status, String desc) {
		ActionHistoryVO entity = new ActionHistoryVO();
		entity.userId = baseAuthority.userId;
		entity.sessionId = baseAuthority.sessionId;
		entity.functionId = baseAuthority.functionId;
		entity.actionDate = DateTimeUtils.getFormatString(new Timestamp(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");
		
		entity.actionDesc = (desc == null ? " " : desc);
		entity.actionStatus = status;
		actionHistoryService.insert(entity);
	}

}
