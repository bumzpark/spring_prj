package skcc.nexcore.client.applicationext.auth;

import skcc.nexcore.client.application.base.BaseAuthority;
import skcc.nexcore.client.application.base.BaseAuthorityVO;
import skcc.nexcore.client.applicationext.dao.MenuGroupMapDAO;
import skcc.nexcore.client.applicationext.entity.MenuGroupMapVO;

public class DefaultAuthority extends BaseAuthority {

	private MenuGroupMapDAO menuGroupMapDAO;

	public void setMenuGroupMapDAO(MenuGroupMapDAO menuGroupMapDAO) {
		this.menuGroupMapDAO = menuGroupMapDAO;
	}

	public void check(BaseAuthorityVO entity) {
		MenuGroupMapVO temp = menuGroupMapDAO.select(entity.groupId, entity.functionId);
		if (temp != null) {
			entity.selectEnabled = "true".equals(temp.selectAction);
			entity.insertEnabled = "true".equals(temp.insertAction);
			entity.updateEnabled = "true".equals(temp.updateAction);
			entity.deleteEnabled = "true".equals(temp.deleteAction);
			entity.serverEnabled = "true".equals(temp.serverAction);
		}
	}

}
