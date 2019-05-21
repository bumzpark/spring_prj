package skcc.nexcore.client.applicationext.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.GroupDAO;
import skcc.nexcore.client.applicationext.dao.MenuGroupMapDAO;
import skcc.nexcore.client.applicationext.entity.MenuGroupMapVO;
import skcc.nexcore.client.applicationext.entity.GroupVO;

@Transactional(readOnly = true)
public class GroupService extends BaseService {

	private GroupDAO groupDAO;
	private MenuGroupMapDAO menuGroupMapDAO;

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setMenuGroupMapDAO(MenuGroupMapDAO menuGroupMapDAO) {
		this.menuGroupMapDAO = menuGroupMapDAO;
	}

	public List<GroupVO> selectAll() throws SQLException {
		return groupDAO.selectAll();
	}

	public GroupVO select(String groupId) throws SQLException {
		return groupDAO.select(groupId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(GroupVO entity) throws SQLException {
		return groupDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(GroupVO entity) throws SQLException {
		return groupDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] groupIds) throws SQLException {
		int row = 0;
		if (groupIds != null) {
			for (String groupId : groupIds) {
				row += groupDAO.delete(groupId);
			}
		}
		return row;
	}

	public List<MenuGroupMapVO> selectGroupMenuMapping(String groupId) {
		return menuGroupMapDAO.selectAll(groupId);
	}

	public MenuGroupMapVO selectGroupMenuMapping(String groupId, String menuId) {
		return menuGroupMapDAO.select(groupId, menuId);
	}

	public List<String> selectAuthorizedList(String groupId) {
		List<String> authorizedList = new ArrayList<String>();

		List<MenuGroupMapVO> list = menuGroupMapDAO.selectAll(groupId);
		if (list != null) {
			for (MenuGroupMapVO entity : list) {
				authorizedList.add(entity.menuId);
			}
		}

		return authorizedList;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insertMenuGroupMapping(String groupId, String[] menuIds) {
		int row = 0;
		menuGroupMapDAO.delete(groupId);
		if (menuIds != null) {
			MenuGroupMapVO entity = null;
			for (String menuId : menuIds) {
				entity = new MenuGroupMapVO();
				entity.groupId = groupId;
				entity.menuId = menuId;
				row += menuGroupMapDAO.insert(entity);
			}
		}
		return row;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insertMenuGroupMapping(String groupId, List<MenuGroupMapVO> entitys) {
		int row = 0;
		menuGroupMapDAO.delete(groupId);
		if (entitys != null) {
			for (MenuGroupMapVO entity : entitys) {
				row += menuGroupMapDAO.insert(entity);
			}
		}
		return row;
	}

}
