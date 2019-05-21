package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.applicationext.entity.GroupVO;

public class GroupDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			GroupVO entity = new GroupVO();
			entity.groupId = result.getString("group_id");
			entity.groupName = result.getString("group_name");
			entity.groupStatus = result.getString("group_status");
			entity.groupDesc = result.getString("group_desc");

			entity.groupNameLocale = result.getString("group_name_locale");
			entity.groupDescLocale = result.getString("group_desc_locale");
			return entity;
		}
	};

	public List<GroupVO> selectAll() throws SQLException {
		String query = "";
		query += " SELECT a.*, b.GROUP_NAME as GROUP_NAME_LOCALE, b.GROUP_DESC AS GROUP_DESC_LOCALE ";
		query += " FROM SKFA_SYS_GROUP a left outer join ( SELECT * FROM SKFA_SYS_GROUP_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.GROUP_ID=b.GROUP_ID ";
		query += " order by a.group_name ";
		return getJdbcTemplate().query(query, new Object[] {}, rowMapper);
	}

	public GroupVO select(String groupId) throws SQLException {
		String query = "";
		query += " SELECT a.*, b.GROUP_NAME as GROUP_NAME_LOCALE, b.GROUP_DESC AS GROUP_DESC_LOCALE ";
		query += " FROM SKFA_SYS_GROUP a left outer join ( SELECT * FROM SKFA_SYS_GROUP_LOCALE WHERE LOCALE='" + Locales.getLocale() + "') b ";
		query += "      on a.GROUP_ID=b.GROUP_ID ";
		query += " where a.group_id=? ";
		return (GroupVO) getFirstObject(getJdbcTemplate().query(query, new Object[] { groupId }, rowMapper));
	}

	public int insert(GroupVO entity) throws SQLException {
		return getJdbcTemplate().update("insert into skfa_sys_group(group_id, group_name, group_status, group_desc) values(?,?,?,?)", new Object[] { entity.groupId, entity.groupName, entity.groupStatus, entity.groupDesc });
	}

	public int update(GroupVO entity) throws SQLException {
		return getJdbcTemplate().update("update skfa_sys_group set group_name=?, group_status=?, group_desc=? where group_id=?", new Object[] { entity.groupName, entity.groupStatus, entity.groupDesc, entity.groupId });
	}

	public int delete(String groupId) throws SQLException {
		return getJdbcTemplate().update("delete from skfa_sys_group where group_id=?", new Object[] { groupId });
	}

}
