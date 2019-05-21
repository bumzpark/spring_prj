package skcc.nexcore.client.applicationext.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.ActionHistoryDAO;
import skcc.nexcore.client.applicationext.entity.ActionHistoryVO;
import skcc.nexcore.client.applicationext.entity.PageNavigation;

@Transactional(readOnly = true)
public class ActionHistoryService extends BaseService {

	private ActionHistoryDAO actionHistoryDAO;

	public void setActionHistoryDAO(ActionHistoryDAO actionHistoryDAO) {
		this.actionHistoryDAO = actionHistoryDAO;
	}

	public int selectTotalCount() {
		return actionHistoryDAO.selectTotalCount();
	}

	public List<ActionHistoryVO> selectAll(PageNavigation pageNavigation) {
		return actionHistoryDAO.selectAll(pageNavigation);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(ActionHistoryVO entity) {
		return actionHistoryDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(List<ActionHistoryVO> list) {
		int row = 0;
		if (list != null) {
			for (ActionHistoryVO entity : list) {
				row += actionHistoryDAO.delete(entity);
			}
		}
		return row;
	}
}
