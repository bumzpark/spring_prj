package skcc.nexcore.client.applicationext.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import skcc.nexcore.client.application.base.BaseDAO;
import skcc.nexcore.client.applicationext.entity.ClobVO;

public class ClobDAO extends BaseDAO {

	private int valueLength = 1000;

	/**
	 * @param splitLength
	 *            the splitLength to set
	 */
	public void setValueLength(int valueLength) {
		this.valueLength = valueLength;
	}

	private RowMapper rowMapper = new RowMapper() {
		public Object mapRow(ResultSet rs, int rownum) throws SQLException {
			ClobInternalVO entity = new ClobInternalVO();
			entity.CLOB_ID = rs.getString("CLOB_ID");
			entity.CLOB_SEQ = rs.getInt("CLOB_SEQ");
			entity.CLOB_VALUE = rs.getString("CLOB_VALUE");
			return entity;
		}
	};

	public int selectTotalCount() {
		String query = "select count(*) as cnt from SKFA_SYS_CLOB ";
		return (Integer) getFirstObject(getJdbcTemplate().query(query, new Object[] {}, totalCountMapper));
	}

	//	public List<ClobVO> selectAll(PageNavigation pageNavigation) {
	//		String query = "";
	//		query += " select * ";
	//		query += " from SKFA_SYS_CLOB ";
	//		query += " order by CLOB_ID ";
	//		return paging(pageNavigation, query, null, rowMapper);
	//	}
	//
	public boolean exist(String CLOB_ID) {
		String query = "";
		query += " select CLOB_ID, 0 as CLOB_SEQ, '' as CLOB_VALUE ";
		query += " from SKFA_SYS_CLOB ";
		query += " where CLOB_ID=? ";
		Object obj = getFirstObject(getJdbcTemplate().query(query, new Object[] { CLOB_ID }, rowMapper));
		if (obj == null) {
			return false;
		}
		return true;
	}

	public ClobVO select(String CLOB_ID) {
		String query = "";
		query += " select * ";
		query += " from SKFA_SYS_CLOB ";
		query += " where CLOB_ID=? ";
		query += " order by CLOB_SEQ asc ";
		List<ClobInternalVO> list = getJdbcTemplate().query(query, new Object[] { CLOB_ID }, rowMapper);
		return merge(list);
	}

	public int insert(ClobVO entity) {
		int row = 0;
		List<ClobInternalVO> list = split(entity);
		if (list.size() > 0) {
			String query = "";
			query += " insert into SKFA_SYS_CLOB ( ";
			query += " CLOB_ID       ";
			query += " , CLOB_SEQ    ";
			query += " , CLOB_VALUE    ";
			query += " ) values(?,?,?) ";

			for (ClobInternalVO internal : list) {
				row += getJdbcTemplate().update(query, new Object[] { internal.CLOB_ID, internal.CLOB_SEQ, internal.CLOB_VALUE });
			}
		}
		return row;
	}

//	public int update(ClobVO entity) {
//		String query = "";
//		query += " update SKFA_SYS_CLOB set ";
//		query += " CLOB_VALUE    =? ";
//		query += " where CLOB_ID=? ";
//		return getJdbcTemplate().update(query, new Object[] { entity.CLOB_VALUE, entity.CLOB_ID });
//	}

	public int delete(String CLOB_ID) {
		String query = "";
		query += " delete from SKFA_SYS_CLOB ";
		query += " where CLOB_ID=? ";
		return getJdbcTemplate().update(query, new Object[] { CLOB_ID });
	}

	private ClobVO merge(List<ClobInternalVO> list) {
		ClobVO result = null;
		if (list != null && list.size() > 0) {
			result = new ClobVO();
			result.CLOB_VALUE = "";
			for (ClobInternalVO entity : list) {
				result.CLOB_ID = entity.CLOB_ID;
				result.CLOB_VALUE += entity.CLOB_VALUE;
			}
		}
		return result;
	}

	private List<ClobInternalVO> split(ClobVO input) {
		List<ClobInternalVO> list = new ArrayList<ClobInternalVO>();
		byte[] bytes = input.CLOB_VALUE.getBytes();
		int length = bytes.length;
		int offset = 0;
		int seq = 0;

		while ((length - offset) > valueLength) {
			ClobInternalVO entity = new ClobInternalVO();
			entity.CLOB_ID = input.CLOB_ID;
			entity.CLOB_SEQ = seq++;
			entity.CLOB_VALUE = new String(bytes, offset, valueLength);
			list.add(entity);

			offset += valueLength;
		}

		if (length - offset > 0) {
			ClobInternalVO entity = new ClobInternalVO();
			entity.CLOB_ID = input.CLOB_ID;
			entity.CLOB_SEQ = seq++;
			entity.CLOB_VALUE = new String(bytes, offset, length - offset);
			list.add(entity);
		}
		return list;
	}

	static class ClobInternalVO {
		String CLOB_ID;
		int CLOB_SEQ;
		String CLOB_VALUE;
	}

}
