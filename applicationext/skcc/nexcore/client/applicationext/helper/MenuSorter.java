package skcc.nexcore.client.applicationext.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skcc.nexcore.client.applicationext.entity.MenuSortedVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;


public class MenuSorter {

	public static List<MenuVO> sort(List<MenuVO> source) {
		if (source != null) {
			List<String> tops = new ArrayList<String>();
			Map<String, MenuSortedVO> temps = new HashMap<String, MenuSortedVO>();

			sortImpl(tops, temps, source);

			List<MenuVO> sorted = new ArrayList<MenuVO>();
			for (String s : tops) {
				sorted.addAll(temps.get(s).getAll());
			}
			return sorted;
		}
		return null;
	}

	public static MenuSortedVO sort(String topId, List<MenuVO> source) {
		if (source != null) {
			List<String> tops = new ArrayList<String>();
			Map<String, MenuSortedVO> temps = new HashMap<String, MenuSortedVO>();

			sortImpl(tops, temps, source);

			MenuSortedVO entity = temps.get(topId);
//			MenuSortedTopVO entity = new MenuSortedTopVO();
//			if (temp != null) {
//				entity.childs = temp.getChildAll();
//			}
			return entity;
		}
		return null;
	}

	private static void sortImpl(List<String> tops, Map<String, MenuSortedVO> temps, List<MenuVO> source) {
		MenuSortedVO parent = null;
		MenuSortedVO current = null;
		for (MenuVO entity : source) {
			current = new MenuSortedVO(entity);
			if (entity.menuLevel == 0) {
				tops.add(entity.menuId);
				entity.topMenuId = entity.menuId;
			} else {
				parent = temps.get(entity.parentId);
				if (parent != null) {
					parent.add(current);
					entity.topMenuId = parent.getMe().topMenuId;
				} else {
					tops.add(entity.menuId);
					entity.topMenuId = entity.menuId;
				}
			}
			temps.put(entity.menuId, current);
		}
	}

}
