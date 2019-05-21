package skcc.nexcore.client.application.base;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import skcc.nexcore.client.applicationext.entity.PageNavigation;

/**
 * 
 * <p>
 * DAO(Data Access Object)의 최상위 클래스
 * </p>
 * 
 * SPRING FRAMEWORK에서 생성한 DataSource 객체를 설정한다.
 * 
 */
public class BaseDAO extends JdbcDaoSupport {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private String productName;

	/**
	 * 리스트형 데이타에서 최초의 값을 추출하는 메소드. SPRING에서 기본 제공하는 queryObject를 이용할 경우 데이타가 없을시
	 * EXCEPTION이 발생하는 문제가 존재. 이를 해결하기 위해 query 메소드를 이용하여 처리 후 데이타 처리
	 * 
	 * @param list
	 *            DB 쿼리 결과
	 * @return 첫번째 VO 객체
	 */
	protected Object getFirstObject(List<?> list) {
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}

	//	public DBMeta getMeta(boolean analysisColumn) throws Exception {
	//		Connection conn = null;
	//		PreparedStatement pstmt = null;
	//		try {
	//			conn = DataSourceUtils.getConnection(getDataSource());
	//			DatabaseMetaData dbmd = conn.getMetaData();
	//			return DBMetaLoader.load(dbmd, analysisColumn);
	//		} finally {
	//			JdbcUtils.closeStatement(pstmt);
	//			DataSourceUtils.releaseConnection(conn, getDataSource());
	//		}
	//	}
	//
	protected String getProductName() {
		if (productName == null) {
			try {
				DatabaseMetaData meta = getConnection().getMetaData();
				productName = meta.getDatabaseProductName();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return productName;
	}

	protected boolean isOracle() {
		return "oracle".equalsIgnoreCase(getProductName());
	}

	protected boolean isMySql() {
		return "mysql".equalsIgnoreCase(getProductName());
	}
	
	protected boolean isDB2(){
		return "DB2".equalsIgnoreCase(getProductName());
	}

	//	public String getURL() {
	//		try {
	//			DatabaseMetaData meta = getConnection().getMetaData();
	//			return meta.getURL();
	//		} catch (Exception e) {
	//			throw new RuntimeException(e);
	//		}
	//	}

	protected RowMapper totalCountMapper = new RowMapper() {
		public Object mapRow(ResultSet result, int rownum) throws SQLException {
			return result.getInt("cnt");
		}
	};

	protected List paging(PageNavigation pageNavigation, String query, Object[] params, RowMapper mapper) {
		String newQuery = getPagingQuery(query);
		return getJdbcTemplate().query(newQuery, getPagingParams(pageNavigation, params), mapper);
	}

	private String getPagingQuery(String query) {
		String newQuery = "";
		if (isOracle()) {
			newQuery += " select *	                           ";
			newQuery += " from (                               ";
			newQuery += "     select rownum sr, sa.*             ";
			newQuery += "     from (                           ";
			newQuery += query;
			newQuery += "     ) sa                              ";
			newQuery += " )                                    ";
			newQuery += " where sr between ? and ?              ";
		} else if (isMySql()) {
			newQuery += query;
			newQuery += " LIMIT ?, ? ";
		}
		return newQuery;
	}

	protected String getBindPagingQuery(PageNavigation pageNavigation, String query) {
		String newQuery = "";
		if (isOracle()) {
			newQuery += " select *	                           ";
			newQuery += " from (                               ";
			newQuery += "     select rownum sr, sa.*             ";
			newQuery += "     from (                           ";
			newQuery += query;
			newQuery += "     ) sa                              ";
			newQuery += " )                                    ";
			newQuery += " where sr between " + pageNavigation.indexStart + " and " + pageNavigation.indexEnd;
		} else if (isMySql()) {
			newQuery += query;
			newQuery += " LIMIT " + pageNavigation.indexStart + "," + pageNavigation.indexEnd;
		}
		return newQuery;
	}

	private Object[] getPagingParams(PageNavigation pageNavigation, Object[] params) {
		Object[] newParams = null;
		if (isOracle()) {
			int len = params == null ? 0 : params.length;
			newParams = new Object[len + 2];
			if (params != null) {
				System.arraycopy(params, 0, newParams, 0, len);
			}
			newParams[len] = pageNavigation.indexStart;
			newParams[len + 1] = pageNavigation.indexEnd;
		} else if (isMySql()) {
			int len = params == null ? 0 : params.length;
			newParams = new Object[len + 2];
			if (params != null) {
				System.arraycopy(params, 0, newParams, 0, len);
			}
			newParams[len] = pageNavigation.limitStart;
			newParams[len + 1] = pageNavigation.limitEnd;
		} else {
			newParams = params;
		}
		return newParams;
	}
}