package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.applicationext.entity.ActionHistoryVO;
import skcc.nexcore.client.applicationext.entity.PageNavigation;

public class ActionHistoryDAO extends BaseDAO {

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			ActionHistoryVO entity = new ActionHistoryVO();
			entity.userId = result.getString("USER_ID");
			entity.sessionId = result.getString("SESSION_ID");
			entity.functionId = result.getString("FUNCTION_ID");

			entity.actionDate = result.getString("ACTION_DATE");
			entity.actionStatus = result.getString("ACTION_STATUS");
			entity.actionDesc = result.getString("ACTION_DESC");

			entity.userName = result.getString("USER_NAME");
			entity.functionName = result.getString("FUNCTION_NAME");
			return entity;
		}
	};

	public List<ActionHistoryVO> selectAll(PageNavigation pageNavigation) {
		String query = "";
		query += " SELECT A.*, B.USER_NAME, (SELECT MENU_NAME FROM SKFA_SYS_MENU_LOCALE WHERE MENU_ID=FUNCTION_ID AND LOCALE='" + Locales.getLocale() + "') as FUNCTION_NAME ";
		query += " FROM SKFA_SYS_ACTION_HIST A LEFT OUTER JOIN SKFA_SYS_USER B ON A.USER_ID=B.USER_ID ";
		query += " order by ACTION_DATE desc ";
		return paging(pageNavigation, query, null, rowMapper);
	}

	public int selectTotalCount() {
		String query = "select count(*) as cnt from SKFA_SYS_ACTION_HIST ";
		return (Integer) getFirstObject(getJdbcTemplate().query(query, new Object[] {}, totalCountMapper));
	}

	public int insert(ActionHistoryVO entity) {
		String query = "";
		query += " insert into SKFA_SYS_ACTION_HIST ";
		query += " (USER_ID, SESSION_ID, FUNCTION_ID, ACTION_DATE, ACTION_STATUS, ACTION_DESC) ";
		query += " values(?,?,?,?,?,?) ";
		return getJdbcTemplate().update(query, new Object[] { entity.userId, entity.sessionId, entity.functionId, entity.actionDate, entity.actionStatus, entity.actionDesc });
	}

	public int delete(ActionHistoryVO entity) {
		String query = "";
		query += " delete from SKFA_SYS_ACTION_HIST where user_id=? and ACTION_DATE=? ";
		return getJdbcTemplate().update(query, new Object[] { entity.userId, entity.actionDate });
	}

}
