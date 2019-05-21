package skcc.nexcore.client.application.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import skcc.nexcore.client.applicationext.entity.PageNavigation;
import skcc.nexcore.client.foundation.util.BytesTokenizer;
import skcc.nexcore.client.foundation.util.StringUtils;

public class DynamicQueryDAO extends BaseDAO {

	public void executeUpdate(String query) throws Exception {
		BytesTokenizer bt = new BytesTokenizer(query.getBytes(), ";".getBytes());
		try {
			while (bt.hasMoreElements()) {
				String sql = bt.nextToString();
				if (sql != null && sql.trim().length() > 0) {
					try {
						int row = getJdbcTemplate().update(sql, new Object[] {});
						logger.info("[SUCCESS] (" + row + ") : \n" + sql);
					} catch (Exception e) {
						logger.info("[FAIL] : \n" + sql + "\n" + StringUtils.getStackTrace(e));
						throw e;
					} finally {
					}
				}
			}
		} finally {
		}
	}

	//	public void executeUpdate(String query) throws Exception {
	//		BytesTokenizer bt = new BytesTokenizer(query.getBytes(), ";".getBytes());
	//		Connection conn = null;
	//		try {
	//			conn = DataSourceUtils.getConnection(getDataSource());
	//			PreparedStatement pstmt = null;
	//			while (bt.hasMoreElements()) {
	//				String sql = bt.nextToString();
	//				if (sql != null && sql.trim().length() > 0) {
	//					try {
	//						pstmt = conn.prepareStatement(sql);
	//						int row = pstmt.executeUpdate();
	//						logger.info("[SUCCESS] (" + row + ") : \n" + sql);
	//					} catch (Exception e) {
	//						logger.info("[FAIL] : \n" + sql + "\n" + StringUtils.getStackTrace(e));
	//						throw e;
	//					} finally {
	//						JdbcUtils.closeStatement(pstmt);
	//					}
	//				}
	//			}
	//		} finally {
	//			DataSourceUtils.releaseConnection(conn, getDataSource());
	//		}
	//	}

	public List<Map<String, Object>> executeQuery(String tableName, String columnNames, String where, String orderBy) throws SQLException {
		return executeQuery(null, tableName, columnNames, where, orderBy);
	}

	public List<Map<String, Object>> executeQuery(PageNavigation pageNavigation, String tableName, String columnNames, String where, String orderBy) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DataSourceUtils.getConnection(getDataSource());

			String query = "";
			query += " SELECT " + columnNames;
			query += " FROM " + tableName;
			if (where != null && where.trim().length() > 0) {
				query += " WHERE " + where;
			}
			if (orderBy != null && orderBy.trim().length() > 0) {
				query += " ORDER BY " + orderBy;
			}

			if (pageNavigation != null) {
				query = getBindPagingQuery(pageNavigation, query);
			}

			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			List<String> columnNameList = getColumnNameList(pstmt);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>();
				for (String columnName : columnNameList) {
					row.put(columnName, rs.getObject(columnName));
				}
				result.add(row);
			}

			return result;
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
	}

	private List<String> getColumnNameList(PreparedStatement pstmt) throws SQLException {
		List<String> columnNameList = new ArrayList<String>();

		ResultSetMetaData rsmd = pstmt.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			columnNameList.add(rsmd.getColumnName(i));
		}
		return columnNameList;
	}

}
