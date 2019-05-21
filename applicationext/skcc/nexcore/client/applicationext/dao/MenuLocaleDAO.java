package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.MenuLocaleVO;

public class MenuLocaleDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			MenuLocaleVO entity = new MenuLocaleVO();
			entity.menuId = result.getString("menu_id");
			entity.locale = result.getString("locale");
			entity.menuName = result.getString("menu_name");
			entity.menuDesc = result.getString("menu_desc");
			return entity;
		}
	};

	public List<MenuLocaleVO> selectAll(String menuId) {
		return getJdbcTemplate().query("select * from skfa_sys_menu_locale where menu_id=?", new Object[] { menuId }, rowMapper);
	}

	public int insert(MenuLocaleVO entity) {
		return getJdbcTemplate().update("insert into skfa_sys_menu_locale(locale, menu_id, menu_name, menu_desc) values(?,?,?,?)", new Object[] { entity.locale, entity.menuId, entity.menuName, entity.menuDesc });
	}

	public int update(MenuLocaleVO entity) {
		return getJdbcTemplate().update("update skfa_sys_menu_locale set menu_name=?, menu_desc=? where locale=? and menu_id=?", new Object[] { entity.menuName, entity.menuDesc, entity.locale, entity.menuId });
	}

	public int delete(String menuId) {
		return getJdbcTemplate().update("delete from skfa_sys_menu_locale where menu_id=?", new Object[] { menuId });
	}

}
