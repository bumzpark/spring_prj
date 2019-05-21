package skcc.nexcore.client.application.configuration.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LabelConfigurationCache {

	private static Map<String, Group> cache = new HashMap<String, Group>();

	public static void add(String filename, String locale, Map<String, String> labels) {
		cache.remove(filename);
		cache.put(filename, new Group(locale, labels));
	}

	public static String get(String locale, String id, String defaultValue) {
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.locale.equals(locale)) {
				if (group.containsKey(id)) {
					return group.get(id);
				}
			}
		}
		return defaultValue;
	}

	static class Group {
		String locale;
		Map<String, String> labels;

		Group(String locale, Map<String, String> labels) {
			this.locale = locale;
			this.labels = labels;
		}

		boolean containsKey(String id) {
			return labels.containsKey(id);
		}

		String get(String id) {
			return labels.get(id);
		}

	}

}
