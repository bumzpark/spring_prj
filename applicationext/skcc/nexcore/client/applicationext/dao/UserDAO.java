package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.UserVO;

public class UserDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			UserVO entity = new UserVO();
			entity.userId = result.getString("user_id");
			entity.userName = result.getString("user_name");
			entity.status = result.getString("status");
			entity.email = result.getString("email");
			entity.password = result.getString("password");
			entity.passwordReset = result.getString("password_reset");
			entity.groupId = result.getString("group_id");
			return entity;
		}
	};

	public List<UserVO> selectAll() throws SQLException {
		return getJdbcTemplate().query("select * from skfa_sys_user order by user_id", new Object[] {}, rowMapper);
	}

	public UserVO select(String userId) throws SQLException {
		return (UserVO) getFirstObject(getJdbcTemplate().query("select * from skfa_sys_user where user_id=?", new Object[] { userId }, rowMapper));
	}

	public int insert(UserVO entity) throws SQLException {
		String query = "";
		query += "insert into skfa_sys_user( ";
		query += "   user_id, user_name, status, email, password, password_reset, group_id ";
		query += " ) values(?,?,?,?,?,?,?) ";
		Object[] prams = new Object[] { entity.userId, entity.userName, entity.status, entity.email, entity.password, entity.passwordReset, entity.groupId };
		return getJdbcTemplate().update(query, prams);
	}

	public int update(UserVO entity) throws SQLException {
		String query = "";
		query += "update skfa_sys_user set ";
		query += "   user_name=?, email=?, group_id=?, status=? ";
		query += " where user_id=? ";

		Object[] prams = new Object[] { entity.userName, entity.email, entity.groupId, entity.status, entity.userId };
		return getJdbcTemplate().update(query, prams);
	}

	public int updateMyInfo(UserVO entity) throws SQLException {
		String query = "";
		query += "update skfa_sys_user set ";
		query += "   user_name=?, email=? ";
		query += " where user_id=? ";
		Object[] prams = new Object[] { entity.userName, entity.email, entity.userId };
		return getJdbcTemplate().update(query, prams);
	}

	public int updatePassword(String userId, String password, String passwordReset) throws SQLException {
		return getJdbcTemplate().update("update skfa_sys_user set password=?, password_reset=? where user_id=? ", new Object[] { password, passwordReset, userId });
	}

	public int updateStatus(String userId, String status) throws SQLException {
		return getJdbcTemplate().update("update skfa_sys_user set status=? where user_id=? ", new Object[] { status, userId });
	}

	public int delete(String userId) throws SQLException {
		return getJdbcTemplate().update("delete from skfa_sys_user where user_id=?", new Object[] { userId });
	}

}
