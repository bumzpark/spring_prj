package skcc.nexcore.client.application.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.applicationext.entity.PageNavigation;

@Transactional(readOnly = true)
public class DynamicQueryService extends BaseService {

	private DynamicQueryDAO dynamicQueryDAO;

	public void setDynamicQueryDAO(DynamicQueryDAO dynamicQueryDAO) {
		this.dynamicQueryDAO = dynamicQueryDAO;
	}

	public int selectTotalCount(String tableName, String columnName, String where) throws SQLException {
		List<Map<String, Object>> result = dynamicQueryDAO.executeQuery(tableName, "COUNT(" + columnName + ") as CNT", where, null);
		if (result != null && result.size() > 0) {
			Map<String, Object> map = result.get(0);
			Object value = map.get("CNT");
			if (value != null) {
				if (value instanceof Number) {
					return ((Number) value).intValue();
				}
			}
		}
		return 0;
	}

	public List<String> selectDistinctStringList(String tableName, String columnName, String where, String orderBy) throws SQLException {
		List<Map<String, Object>> temp = dynamicQueryDAO.executeQuery(tableName, "DISTINCT " + columnName, where, orderBy);
		if (temp == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (Map<String, Object> map : temp) {
			String value = (String) map.get(columnName);
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}

	public List<Map<String, Object>> selectAll(PageNavigation pageNavigation, String tableName, String columnNames, String where, String orderBy) throws SQLException {
		return dynamicQueryDAO.executeQuery(pageNavigation, tableName, columnNames, where, orderBy);
	}

}
