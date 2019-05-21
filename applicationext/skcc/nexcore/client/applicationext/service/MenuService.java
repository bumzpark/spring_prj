package skcc.nexcore.client.applicationext.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.MenuDAO;
import skcc.nexcore.client.applicationext.entity.MenuSortedVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;
import skcc.nexcore.client.applicationext.helper.MenuSorter;

@Transactional(readOnly = true)
public class MenuService extends BaseService {

	private MenuDAO menuDAO;

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public List<MenuVO> selectAll() {
		return MenuSorter.sort(menuDAO.selectAll());
	}

	public List<MenuVO> selectTopAll() {
		return menuDAO.selectAllByLevel(0);
	}

	public List<MenuVO> selectDirAll() {
		return MenuSorter.sort(menuDAO.selectDirAll());
	}

	// public List<MenuVO> selectSubAll(String menuId) {
	// if (menuId == null || menuId.length() < 1) {
	// return null;
	// }
	// return MenuSorter.sort(menuDAO.selectSub(menuId));
	// }

	public MenuVO select(String menuId) {
		return menuDAO.select(menuId);
	}

	public List<String> selectMenuName(String menuId) {
		return menuDAO.selectMenuName(menuId);
	}

	public MenuVO selectGroupMenu(String groupId, String menuId) {
		return menuDAO.selectGroupMenu(groupId, menuId);
	}

	public List<MenuVO> selectGroupMenuAll(String groupId) {
		return MenuSorter.sort(menuDAO.selectGroupMenu(groupId));
	}

	//	public String getDataBaseURL() {
	//		return menuDAO.getURL();
	//	}

	public List<MenuVO> selectGroupTopAll(String groupId) {
		return menuDAO.selectGroupMenu(groupId, 0);
	}

	public MenuSortedVO selectGroupSubAll(String topMenuId, String groupId) {
		return MenuSorter.sort(topMenuId, menuDAO.selectGroupMenu(groupId));
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(MenuVO entity) {
		return menuDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(MenuVO entity) {
		return menuDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int updateOrderNo(String[] menuIds, String[] orderNos) {
		int row = 0;
		if (menuIds != null && orderNos != null) {
			int len = menuIds.length;
			for (int i = 0; i < len; i++) {
				row += menuDAO.updateOrderNo(menuIds[i], toInt(orderNos[i]));
			}
		}
		return row;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] menuIds) {
		int row = 0;
		if (menuIds != null) {
			for (String menuId : menuIds) {
				row += menuDAO.delete(menuId);
			}
		}
		return row;
	}

	private int toInt(String str) {
		if (str == null || str.trim().length() < 1) {
			return 0;
		}
		return Integer.parseInt(str);
	}
}
