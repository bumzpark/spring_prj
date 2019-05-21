package skcc.nexcore.client.application.database;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.annotation.Transactional;

import skcc.nexcore.client.application.database.entity.DatabaseTableMeta;

@Transactional(readOnly = true)
public class DatabaseExporter {

	public DatabaseTableMeta getMeta(DataSource dataSource, String catalog, String schema) throws SQLException {
		Connection conn = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			DatabaseMetaData dbmd = conn.getMetaData();

			List<String> catalogList = new ArrayList<String>();
			Map<String, List<String>> catalogTables = new HashMap<String, List<String>>();

			List<String> schemaList = new ArrayList<String>();
			Map<String, List<String>> schemaTables = new HashMap<String, List<String>>();

			getCatalogs(catalogList, catalogTables, catalog, dbmd);
			getSchemas(schemaList, schemaTables, schema, dbmd);

			DatabaseTableMeta entity = new DatabaseTableMeta();
			entity.catalogs = catalogList;
			entity.catalogTables = catalogTables;

			entity.schemas = schemaList;
			entity.schemaTables = schemaTables;
			return entity;
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	public void export(DataSource dataSource, String[] tableNames, OutputStream os) throws SQLException, IOException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);

			try {
				for (String tableName : tableNames) {
					String title = "";
					title += "/******************************************************************/\n";
					title += "/** " + tableName + " **/\n";
					title += "/******************************************************************/\n";
					os.write(title.getBytes());

					String query = "SELECT * FROM " + tableName;
					pstmt = conn.prepareStatement(query);
					rs = pstmt.executeQuery();

					List<Integer> columnTypes = new ArrayList<Integer>();
					StringBuilder insertHeaderBuff = new StringBuilder();
					insertHeaderBuff.append("INSERT INTO ").append(tableName).append(" ( ");
					ResultSetMetaData rsmd = pstmt.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						columnTypes.add(rsmd.getColumnType(i));
						insertHeaderBuff.append(rsmd.getColumnName(i));
						if (i < columnCount) {
							insertHeaderBuff.append(", ");
						}
					}
					insertHeaderBuff.append(" ) VALUES ( ");
					String insertHeader = insertHeaderBuff.toString();

					StringBuilder valueBuff = new StringBuilder();
					while (rs.next()) {
						valueBuff.setLength(0);
						for (int i = 1; i <= columnCount; i++) {
							valueBuff.append("'").append(convertValue(columnTypes.get(i - 1), rs.getObject(i))).append("'");
							if (i < columnCount) {
								valueBuff.append(", ");
							}
						}
						valueBuff.append(");\n");
						os.write((insertHeader + valueBuff.toString()).getBytes());
					}

					os.write(("\n").getBytes());
				}
			} finally {
				JdbcUtils.closeResultSet(rs);
				JdbcUtils.closeStatement(pstmt);
			}
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	private Object convertValue(int type, Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof String) {
			String str = (String) value;
			return StringUtils.replace(str, "'", "''");
		}
		return value;
	}

	private void getCatalogs(List<String> catalogList, Map<String, List<String>> tableMap, String inputCatalog, DatabaseMetaData dbmd) throws SQLException {
		if(inputCatalog != null){
			if(inputCatalog.trim().length() < 1){
				inputCatalog = null;
			}
		}
		
		ResultSet rs = null;
		try {
			rs = dbmd.getCatalogs();
			while (rs.next()) {
				String catalog = rs.getString("TABLE_CAT");
				catalogList.add(catalog);
				if (inputCatalog == null || (inputCatalog != null && inputCatalog.length() > 0 && inputCatalog.equals(catalog))) {
					List<String> tables = getTables(dbmd, catalog, null);
					if (tables != null) {
						List<String> tableList = tableMap.get(catalog);
						if (tableList == null) {
							tableList = new ArrayList<String>();
							tableMap.put(catalog, tableList);
						}
						for (String tableName : tables) {
							tableList.add(tableName);
						}
					}
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void getSchemas(List<String> schemaList, Map<String, List<String>> tableMap, String inputSchema, DatabaseMetaData dbmd) throws SQLException {
		if(inputSchema != null){
			if(inputSchema.trim().length() < 1){
				inputSchema = null;
			}
		}

		ResultSet rs = null;
		try {
			rs = dbmd.getSchemas();
			while (rs.next()) {
				String schema = rs.getString("TABLE_SCHEM");
				schemaList.add(schema);
				if (inputSchema == null || (inputSchema != null && inputSchema.length() > 0 && inputSchema.equals(schema))) {
					List<String> tables = getTables(dbmd, null, schema);
					if (tables != null) {
						List<String> tableList = tableMap.get(schema);
						if (tableList == null) {
							tableList = new ArrayList<String>();
							tableMap.put(schema, tableList);
						}
						for (String tableName : tables) {
							tableList.add(tableName);
						}
					}
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private List<String> getTables(DatabaseMetaData dbmd, String catalog, String schema) throws SQLException {
		List<String> tables = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = dbmd.getTables(catalog, schema, null, new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				tables.add(tableName);
			}
			return tables;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

}
