package skcc.nexcore.client.application.configuration.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageConfigurationCache {

	private static Map<String, Group> cache = new HashMap<String, Group>();

	public static void add(String filename, String locale, Map<String, String> messages) {
		cache.remove(filename);
		cache.put(filename, new Group(locale, messages));
	}

	public static String get(String locale, String id) {
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.locale.equals(locale)) {
				if (group.containsKey(id)) {
					return group.get(id);
				}
			}
		}
		return null;
	}

	static class Group {
		String locale;
		Map<String, String> messages;

		Group(String locale, Map<String, String> messages) {
			this.locale = locale;
			this.messages = messages;
		}

		boolean containsKey(String id) {
			return messages.containsKey(id);
		}

		String get(String id) {
			return messages.get(id);
		}

	}

}
