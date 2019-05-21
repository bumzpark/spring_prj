package skcc.nexcore.client.applicationext.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.LoginHistoryDAO;
import skcc.nexcore.client.applicationext.entity.LoginHistoryVO;
import skcc.nexcore.client.applicationext.entity.PageNavigation;

@Transactional(readOnly = true)
public class LoginHistoryService extends BaseService {

	private LoginHistoryDAO loginHistoryDAO;

	public void setLoginHistoryDAO(LoginHistoryDAO loginHistoryDAO) {
		this.loginHistoryDAO = loginHistoryDAO;
	}

	public int selectTotalCount() {
		return loginHistoryDAO.selectTotalCount();
	}

	public List<LoginHistoryVO> selectAll(PageNavigation pageNavigation) {
		return loginHistoryDAO.selectAll(pageNavigation);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(LoginHistoryVO entity) {
		return loginHistoryDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(List<LoginHistoryVO> list) {
		int row = 0;
		if (list != null) {
			for (LoginHistoryVO entity : list) {
				row += loginHistoryDAO.delete(entity);
			}
		}
		return row;
	}

}
