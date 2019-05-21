package skcc.nexcore.client.applicationext.entity;

import java.util.List;

import skcc.nexcore.client.application.base.BaseVO;

public class DataExportVO extends BaseVO {

	private static final long serialVersionUID = 2896139970671696388L;

	public List<String> catalogs;
	public List<String> schemas;
	public List<DataExportTableVO> tables;

}
