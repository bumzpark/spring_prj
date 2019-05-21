package skcc.nexcore.client.applicationext.entity;

import skcc.nexcore.client.application.base.BaseVO;

public class MenuVO extends BaseVO {

	private static final long serialVersionUID = 8642301723833994360L;

	public String menuId;
	public String menuName;
	public String menuUrl;
	public int menuLevel;
	public String parentId;
	public int orderNo;
	public String menuDesc;

	public String menuNameLocale;
	public String menuDescLocale;

	public boolean existSub;
	public String topMenuId;

}
