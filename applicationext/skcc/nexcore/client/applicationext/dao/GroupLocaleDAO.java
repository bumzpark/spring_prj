package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.GroupLocaleVO;

public class GroupLocaleDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			GroupLocaleVO entity = new GroupLocaleVO();
			entity.groupId = result.getString("group_id");
			entity.locale = result.getString("locale");
			entity.groupName = result.getString("group_name");
			entity.groupDesc = result.getString("group_desc");
			return entity;
		}
	};

	public List<GroupLocaleVO> selectAll(String groupId) throws SQLException {
		return getJdbcTemplate().query("select * from skfa_sys_group_locale where group_id=?", new Object[] { groupId }, rowMapper);
	}

	public int insert(GroupLocaleVO entity) throws SQLException {
		return getJdbcTemplate().update("insert into skfa_sys_group_locale(group_id, locale, group_name, group_desc) values(?,?,?,?)", new Object[] { entity.groupId, entity.locale, entity.groupName, entity.groupDesc });
	}

	public int update(GroupLocaleVO entity) throws SQLException {
		return getJdbcTemplate().update("update skfa_sys_group_locale set group_name=?, group_desc=? where group_id=? and locale=?", new Object[] { entity.groupName, entity.groupDesc, entity.groupId, entity.locale });
	}

	public int delete(String groupId) throws SQLException {
		return getJdbcTemplate().update("delete from skfa_sys_group_locale where group_id=?", new Object[] { groupId });
	}

}
