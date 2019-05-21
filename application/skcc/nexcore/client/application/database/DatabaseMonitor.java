package skcc.nexcore.client.application.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import skcc.nexcore.client.application.database.entity.DatabaseMeta;

public class DatabaseMonitor {

	private Map<String, DataSource> dataSourceMap; //<name, DataSource>

	/**
	 * @param dataSourceMap
	 *            the dataSourceMap to set
	 */
	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public List<String> getNames() {
		if (dataSourceMap != null) {
			return new ArrayList<String>(dataSourceMap.keySet());
		}
		return null;
	}

	public List<DatabaseMeta> getAll() throws SQLException {
		List<DatabaseMeta> metas = new ArrayList<DatabaseMeta>();

		if (dataSourceMap != null) {
			Iterator<String> keys = dataSourceMap.keySet().iterator();
			String name = null;
			while (keys.hasNext()) {
				name = keys.next();
				metas.add(getMeta(name, dataSourceMap.get(name)));
			}
		}
		return metas;
	}

	private DatabaseMeta getMeta(String name, DataSource dataSource) throws SQLException {
		Connection conn = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			DatabaseMetaData dbmd = conn.getMetaData();
			DatabaseMeta entity = new DatabaseMeta();
			entity.name = name;
			entity.databaseMinorVersion = dbmd.getDatabaseMinorVersion();
			entity.databaseMajorVersion = dbmd.getDatabaseMajorVersion();
			entity.databaseProductName = dbmd.getDatabaseProductName();
			entity.databaseProductVersion = dbmd.getDatabaseProductVersion();
			entity.driverName = dbmd.getDriverName();
			entity.driverVersion = dbmd.getDriverVersion();
			entity.url = dbmd.getURL();
			entity.userName = dbmd.getUserName();
			return entity;
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	public DataSource getDataSource(String name) throws SQLException {
		if (dataSourceMap == null) {
			return null;
		}
		return dataSourceMap.get(name);
	}

}
