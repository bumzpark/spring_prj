package skcc.nexcore.client.applicationext.entity;

import java.util.ArrayList;
import java.util.List;

import skcc.nexcore.client.application.base.BaseVO;

public class MenuSortedVO extends BaseVO {

	private static final long serialVersionUID = 1094047599851143055L;

	private MenuVO me;
	private List<MenuSortedVO> childs = new ArrayList<MenuSortedVO>();

	public MenuSortedVO(MenuVO me) {
		this.me = me;
	}

	public void add(MenuSortedVO child) {
		childs.add(child);
		me.existSub = true;
	}

	public List<MenuVO> getAll() {
		List<MenuVO> temp = new ArrayList<MenuVO>();
		temp.add(me);
		for (MenuSortedVO t : childs) {
			temp.addAll(t.getAll());
		}
		return temp;
	}

	public List<MenuVO> getChildAll() {
		List<MenuVO> temp = new ArrayList<MenuVO>();
		for (MenuSortedVO t : childs) {
			temp.addAll(t.getAll());
		}
		return temp;
	}

	public MenuVO getMe() {
		return me;
	}

	public List<MenuSortedVO> getChilds() {
		return childs;
	}

}
