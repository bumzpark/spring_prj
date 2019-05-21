package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.applicationext.entity.MenuVO;

public class MenuDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			MenuVO entity = new MenuVO();
			entity.menuId = result.getString("menu_id");
			entity.menuUrl = result.getString("menu_url");
			entity.parentId = result.getString("parent_id");
			entity.menuLevel = result.getInt("menu_level");
			entity.orderNo = result.getInt("order_no");

			entity.menuName = result.getString("menu_name");
			entity.menuDesc = result.getString("menu_desc");

			entity.menuNameLocale = result.getString("menu_name_locale");
			entity.menuDescLocale = result.getString("menu_desc_locale");

			return entity;
		}
	};

	private RowMapper menuNameMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			String name = result.getString("menu_name_locale");
			if (name == null || name.trim().length() < 1) {
				name = result.getString("menu_name");
			}
			return name;
		}
	};

	public List<MenuVO> selectAll() {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " order by a.menu_level, a.order_no, a.menu_name ";
		return getJdbcTemplate().query(query, new Object[] {}, rowMapper);
	}

	public List<MenuVO> selectAllByLevel(int menuLevel) {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_level=? ";
		query += " order by a.order_no, a.menu_name ";
		return getJdbcTemplate().query(query, new Object[] { menuLevel }, rowMapper);
	}

	public List<MenuVO> selectDirAll() {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where menu_url is null or menu_url = '' ";
		query += " order by a.menu_level, a.order_no, a.menu_name ";
		return getJdbcTemplate().query(query, new Object[] {}, rowMapper);
	}

	public MenuVO select(String menuId) {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id=? ";
		return (MenuVO) getFirstObject(getJdbcTemplate().query(query, new Object[] { menuId }, rowMapper));
	}

	public List<String> selectMenuName(String menuId) {
		String query = "";
		query += " SELECT a.MENU_NAME, b.MENU_NAME as MENU_NAME_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id in (select parent_id from skfa_sys_menu where menu_id=?) ";
		query += " union all ";
		query += " SELECT a.MENU_NAME, b.MENU_NAME as MENU_NAME_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id = ?";
		return getJdbcTemplate().query(query, new Object[] { menuId, menuId }, menuNameMapper);
	}

	public int insert(MenuVO entity) {
		return getJdbcTemplate().update("insert into skfa_sys_menu(menu_id, menu_name, menu_url, menu_level, parent_id, order_no, menu_desc) values(?,?,?,?,?,?,?)", new Object[] { entity.menuId, entity.menuName, entity.menuUrl, entity.menuLevel, entity.parentId, entity.orderNo, entity.menuDesc });
	}

	public int update(MenuVO entity) {
		return getJdbcTemplate().update("update skfa_sys_menu set menu_name=?, menu_url=?, menu_level=?, parent_id=?, menu_desc=? where menu_id=?", new Object[] { entity.menuName, entity.menuUrl, entity.menuLevel, entity.parentId, entity.menuDesc, entity.menuId });
	}

	public int updateOrderNo(String menuId, int orderNo) {
		return getJdbcTemplate().update("update skfa_sys_menu set order_no=? where menu_id=?", new Object[] { orderNo, menuId });
	}

	public int delete(String menuId) {
		return getJdbcTemplate().update("delete from skfa_sys_menu where menu_id=?", new Object[] { menuId });
	}

	public MenuVO selectGroupMenu(String groupId, String menuId) {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id in (select menu_id from skfa_sys_menu_group_map where group_id=? and select_action='true' and menu_id=?) ";
		return (MenuVO) getFirstObject(getJdbcTemplate().query(query, new Object[] { groupId, menuId }, rowMapper));
	}

	public List<MenuVO> selectGroupMenu(String groupId, int menuLevel) {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id in (select menu_id from skfa_sys_menu_group_map where group_id=? and select_action='true') ";
		query += "   and a.menu_level=? ";
		query += " order by a.order_no, a.menu_name ";
		return getJdbcTemplate().query(query, new Object[] { groupId, menuLevel }, rowMapper);
	}

	public List<MenuVO> selectGroupMenu(String groupId) {
		String query = "";
		query += " SELECT a.*, b.MENU_NAME as MENU_NAME_LOCALE, b.MENU_DESC AS MENU_DESC_LOCALE ";
		query += " FROM SKFA_SYS_MENU a left outer join (SELECT * FROM SKFA_SYS_MENU_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.MENU_ID=b.MENU_ID ";
		query += " where a.menu_id in (select menu_id from skfa_sys_menu_group_map where group_id=? and select_action='true') ";
		query += " order by a.menu_level, a.order_no, a.menu_name ";
		return getJdbcTemplate().query(query, new Object[] { groupId }, rowMapper);
	}

}
