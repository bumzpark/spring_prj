package skcc.nexcore.client.application.database.entity;

import java.util.List;
import java.util.Map;

import skcc.nexcore.client.application.base.BaseVO;

public final class DatabaseTableMeta extends BaseVO {

	private static final long serialVersionUID = 2896139970671696388L;

	public List<String> catalogs;
	public List<String> schemas;

	public Map<String, List<String>> catalogTables;
	public Map<String, List<String>> schemaTables;

}
