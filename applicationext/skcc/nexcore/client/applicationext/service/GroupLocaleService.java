package skcc.nexcore.client.applicationext.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.GroupLocaleDAO;
import skcc.nexcore.client.applicationext.entity.GroupLocaleVO;

@Transactional(readOnly = true)
public class GroupLocaleService extends BaseService {

	private GroupLocaleDAO groupLocaleDAO;

	public void setGroupLocaleDAO(GroupLocaleDAO groupLocaleDAO) {
		this.groupLocaleDAO = groupLocaleDAO;
	}

	public List<GroupLocaleVO> selectAll(String groupId) throws SQLException {
		return groupLocaleDAO.selectAll(groupId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(List<GroupLocaleVO> list) throws SQLException {
		int row = 0;
		if (list != null) {
			for (GroupLocaleVO entity : list) {
				row += groupLocaleDAO.insert(entity);
			}
		}
		return row;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(GroupLocaleVO entity) throws SQLException {
		return groupLocaleDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(GroupLocaleVO entity) throws SQLException {
		return groupLocaleDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] groupIds) throws SQLException {
		int row = 0;
		if (groupIds != null) {
			for (String groupId : groupIds) {
				row += groupLocaleDAO.delete(groupId);
			}
		}
		return row;
	}

}
