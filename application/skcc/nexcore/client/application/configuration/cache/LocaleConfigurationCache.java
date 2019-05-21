package skcc.nexcore.client.application.configuration.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import skcc.nexcore.client.application.util.LocaleObject;

public class LocaleConfigurationCache {

	private static Map<String, Map<String, LocaleObject>> groups = new HashMap<String, Map<String, LocaleObject>>();

	public static void add(String filename, Map<String, LocaleObject> locales) {
		groups.remove(filename);
		groups.put(filename, locales);
	}

	public static LocaleObject getDefault() {
		Iterator<Map<String, LocaleObject>> groupvalues = groups.values().iterator();
		while (groupvalues.hasNext()) {
			Map<String, LocaleObject> labels = groupvalues.next();
			Iterator<LocaleObject> labelvalues = labels.values().iterator();
			while (labelvalues.hasNext()) {
				LocaleObject l = labelvalues.next();
				if (l.isDefault) {
					return l;
				}
			}
		}
		return null;
	}

	public static List<LocaleObject> getAll() {
		List<LocaleObject> list = new ArrayList<LocaleObject>();
		Iterator<Map<String, LocaleObject>> maps = groups.values().iterator();
		while (maps.hasNext()) {
			Map<String, LocaleObject> map = maps.next();
			list.addAll(map.values());
		}
		return list;
	}

	public static LocaleObject get(String id) {
		Iterator<Map<String, LocaleObject>> maps = groups.values().iterator();
		while (maps.hasNext()) {
			Map<String, LocaleObject> map = maps.next();
			if (map.containsKey(id)) {
				return map.get(id);
			}
		}
		return null;
	}

}
