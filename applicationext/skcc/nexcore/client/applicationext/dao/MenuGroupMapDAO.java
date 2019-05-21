package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.MenuGroupMapVO;

public class MenuGroupMapDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			MenuGroupMapVO entity = new MenuGroupMapVO();
			entity.groupId = result.getString("group_id");
			entity.menuId = result.getString("menu_id");
			entity.selectAction = result.getString("select_action");
			entity.insertAction = result.getString("insert_action");
			entity.updateAction = result.getString("update_action");
			entity.deleteAction = result.getString("delete_action");
			entity.serverAction = result.getString("server_action");
			return entity;
		}
	};

	public List<MenuGroupMapVO> selectAll() {
		return getJdbcTemplate().query("select * from skfa_sys_menu_group_map order by group_id, menu_id", new Object[] {}, rowMapper);
	}

	public List<MenuGroupMapVO> selectAll(String groupId) {
		return getJdbcTemplate().query("select * from skfa_sys_menu_group_map where group_id=? order by menu_id", new Object[] { groupId }, rowMapper);
	}

	public MenuGroupMapVO select(String groupId, String menuId) {
		return (MenuGroupMapVO) getFirstObject(getJdbcTemplate().query("select * from skfa_sys_menu_group_map where group_id=? and menu_id=?", new Object[] { groupId, menuId }, rowMapper));
	}

	public MenuGroupMapVO selectByFunctionId(String groupId, String functionId) {
		String query = "";
		query += " select b.* ";
		query += " from skfa_sys_menu_group_map a, akfa_sys_menu b ";
		query += " where a.group_id=? ";
		query += "   and a.select_action='true' ";
		query += "   and a.menu_id=b.menu_id ";
		query += " order by b.menu_level, b.order_no, b.menu_name ";
		return (MenuGroupMapVO) getFirstObject(getJdbcTemplate().query(query, new Object[] { groupId, functionId }, rowMapper));
	}

	public int delete(String groupId) {
		return getJdbcTemplate().update("delete from skfa_sys_menu_group_map where group_id=?", new Object[] { groupId });
	}

	public int insert(MenuGroupMapVO entity) {
		return getJdbcTemplate().update("insert into skfa_sys_menu_group_map(group_id, menu_id, select_action, insert_action, update_action, delete_action, server_action ) values(?,?,?,?,?,?,?)", new Object[] { entity.groupId, entity.menuId, entity.selectAction, entity.insertAction, entity.updateAction, entity.deleteAction, entity.serverAction });
	}

}
