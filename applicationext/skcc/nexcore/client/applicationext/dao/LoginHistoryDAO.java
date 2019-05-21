package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.LoginHistoryVO;
import skcc.nexcore.client.applicationext.entity.PageNavigation;

public class LoginHistoryDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			LoginHistoryVO entity = new LoginHistoryVO();
			entity.userId = result.getString("user_id");
			entity.userName = result.getString("user_name");
			entity.loginStatus = result.getString("login_status");
			entity.loginDate = result.getString("login_date");
			entity.sessionId = result.getString("session_id");
			entity.clientHostAddr = result.getString("client_host_addr");
			entity.groupId = result.getString("group_id");
			return entity;
		}
	};

	public List<LoginHistoryVO> selectAll(PageNavigation pageNavigation) {
		String query = "";
		query += " SELECT A.*, B.USER_NAME ";
		query += " FROM skfa_sys_user_login_hist A LEFT OUTER JOIN SKFA_SYS_USER B ON A.USER_ID=B.USER_ID ";
		query += " order by login_date desc ";
		return paging(pageNavigation, query, null, rowMapper);
	}

	public int selectTotalCount() {
		String query = "select count(*) as cnt from skfa_sys_user_login_hist ";
		return (Integer) getFirstObject(getJdbcTemplate().query(query, new Object[] {}, totalCountMapper));
	}

	public int insert(LoginHistoryVO entity) {
		String query = "";
		query += " insert into skfa_sys_user_login_hist ";
		query += " (user_id, login_date, session_id, login_status, client_host_addr, group_id) ";
		query += " values(?,?,?,?,?,?) ";

		return getJdbcTemplate().update(query, new Object[] { entity.userId, entity.loginDate, entity.sessionId, entity.loginStatus, entity.clientHostAddr, entity.groupId });
	}

	public int delete(LoginHistoryVO entity) {
		String query = "";
		query += " delete from skfa_sys_user_login_hist where user_id=? and login_date=? ";
		return getJdbcTemplate().update(query, new Object[] { entity.userId, entity.loginDate });
	}

}
