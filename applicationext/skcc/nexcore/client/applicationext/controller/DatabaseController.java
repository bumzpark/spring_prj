package skcc.nexcore.client.applicationext.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.database.DatabaseExporter;
import skcc.nexcore.client.application.database.DatabaseMonitor;
import skcc.nexcore.client.application.database.entity.DatabaseMeta;
import skcc.nexcore.client.application.database.entity.DatabaseTableMeta;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;

public class DatabaseController extends BaseController {

	private DatabaseMonitor databaseMonitor;
	private DatabaseExporter databaseExporter;

	/**
	 * @param databaseMonitor
	 *            the dataSourceMonitor to set
	 */
	public void setDataSourceMonitor(DatabaseMonitor databaseMonitor) {
		this.databaseMonitor = databaseMonitor;
	}

	/**
	 * @param databaseMonitor
	 *            the databaseMonitor to set
	 */
	public void setDatabaseMonitor(DatabaseMonitor databaseMonitor) {
		this.databaseMonitor = databaseMonitor;
	}

	/**
	 * @param databaseExporter
	 *            the databaseExporter to set
	 */
	public void setDatabaseExporter(DatabaseExporter databaseExporter) {
		this.databaseExporter = databaseExporter;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}
		List<DatabaseMeta> database_meta_list = databaseMonitor.getAll();

		ModelAndView mav = new ModelAndView("forward:/system/admin/databaseList.jsp");
		mav.addObject("database_meta_list", database_meta_list);
		return mav;
	}

	public ModelAndView exportView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}
		String database_name = request.getParameter("database_name");
		String schema = request.getParameter("schema");
		String catalog = request.getParameter("catalog");

		DatabaseTableMeta database_table_meta = null;
		if (database_name != null && database_name.trim().length() > 0) {
			DataSource dataSource = databaseMonitor.getDataSource(database_name);
			database_table_meta = databaseExporter.getMeta(dataSource, catalog, schema);
		}

		ModelAndView mav = new ModelAndView("forward:/system/admin/databaseExport.jsp");
		mav.addObject("database_table_meta", database_table_meta);
		mav.addObject("selected_database_name", database_name);
		mav.addObject("selected_catalog", catalog);
		mav.addObject("selected_schema", schema);
		return mav;
	}

	public ModelAndView export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String database_name = request.getParameter("database_name");
		String[] idList = request.getParameterValues("id_list");

		String fileName = database_name + ".export";
		response.setContentType("application/vnd.ms-excel; charset=euc-kr");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		OutputStream os = response.getOutputStream();
		if (idList != null) {
			DataSource dataSource = databaseMonitor.getDataSource(database_name);
			databaseExporter.export(dataSource, idList, os);
		}
		os.flush();
		os.close();
		return null;
	}

}
