package skcc.nexcore.client.applicationext.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.base.BaseService;
import skcc.nexcore.client.applicationext.dao.MenuLocaleDAO;
import skcc.nexcore.client.applicationext.entity.MenuLocaleVO;

@Transactional(readOnly = true)
public class MenuLocaleService extends BaseService {

	private MenuLocaleDAO menuLocaleDAO;

	public void setMenuLocaleDAO(MenuLocaleDAO menuLocaleDAO) {
		this.menuLocaleDAO = menuLocaleDAO;
	}

	public List<MenuLocaleVO> selectAll(String menuId) {
		return menuLocaleDAO.selectAll(menuId);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(List<MenuLocaleVO> list) {
		int row = 0;
		if (list != null) {
			for (MenuLocaleVO entity : list) {
				row += menuLocaleDAO.insert(entity);
			}
		}
		return row;
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int insert(MenuLocaleVO entity) {
		return menuLocaleDAO.insert(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int update(MenuLocaleVO entity) {
		return menuLocaleDAO.update(entity);
	}

	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public int delete(String[] menuIds) {
		int row = 0;
		if (menuIds != null) {
			for (String menuId : menuIds) {
				row += menuLocaleDAO.delete(menuId);
			}
		}
		return row;
	}

}
