package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.ScheduleVO;

public class ScheduleDAO extends BaseDAO {

	/**
	 * JOB_ID JOB_URL JOB_CRON_EXPRESSION JOB_PARAMS JOB_ENABLED
	 */

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet rs, int rownum) throws SQLException {
			ScheduleVO entity = new ScheduleVO();
			entity.jobId = rs.getString("JOB_ID");
			entity.jobName = rs.getString("JOB_NAME");
			entity.jobDesc = rs.getString("JOB_DESC");
			entity.jobUrl = rs.getString("JOB_URL");
			entity.jobCronExpression = rs.getString("JOB_CRON_EXPRESSION");
			entity.jobParams = rs.getString("JOB_PARAMS");
			entity.jobEnabled = rs.getString("JOB_ENABLED");
			return entity;
		}
	};

	public List<ScheduleVO> selectAll() {
		String query = "";
		query += " select * ";
		query += " from SKFA_SYS_SCHEDULE ";
		query += " order by JOB_ID ";
		return getJdbcTemplate().query(query, new Object[] {}, rowMapper);
	}

	public ScheduleVO select(String jobId) {
		String query = "";
		query += " select * ";
		query += " from SKFA_SYS_SCHEDULE ";
		query += " where JOB_ID=? ";
		return (ScheduleVO) getFirstObject(getJdbcTemplate().query(query, new Object[] { jobId }, rowMapper));
	}

	public int insert(ScheduleVO entity) {
		String query = "";
		query += " insert into SKFA_SYS_SCHEDULE ( ";
		query += " JOB_ID, JOB_NAME, JOB_DESC, JOB_URL, JOB_CRON_EXPRESSION, JOB_PARAMS, JOB_ENABLED ";
		query += " ) values(?,?,?,?,?,?,?) ";
		return getJdbcTemplate().update(query, new Object[] { entity.jobId, entity.jobName, entity.jobDesc, entity.jobUrl, entity.jobCronExpression, entity.jobParams, entity.jobEnabled });
	}

	public int update(ScheduleVO entity) {
		String query = "";
		query += " update SKFA_SYS_SCHEDULE set ";
		query += "  JOB_NAME=?, JOB_DESC=?, JOB_URL=?, JOB_CRON_EXPRESSION=?, JOB_PARAMS=?, JOB_ENABLED=? ";
		query += " where JOB_ID=? ";
		return getJdbcTemplate().update(query, new Object[] { entity.jobName, entity.jobDesc, entity.jobUrl, entity.jobCronExpression, entity.jobParams, entity.jobEnabled, entity.jobId });
	}

	public int delete(String jobId) {
		String query = "";
		query += " delete from SKFA_SYS_SCHEDULE ";
		query += " where JOB_ID=? ";
		return getJdbcTemplate().update(query, new Object[] { jobId });
	}

}
