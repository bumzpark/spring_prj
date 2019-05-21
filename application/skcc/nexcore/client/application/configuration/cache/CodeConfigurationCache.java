package skcc.nexcore.client.application.configuration.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import skcc.nexcore.client.application.util.Code;

public class CodeConfigurationCache {

	private static Map<String, Group> cache = new HashMap<String, Group>();

	public static void add(String filename, String locale, Map<String, List<Code>> groups) {
		cache.remove(filename);
		cache.put(filename, new Group(locale, groups));
	}

	public static String get(String locale, String groupId, String codeId, String defaultValue) {
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.locale.equals(locale)) {
				if (group.containsKey(groupId, codeId)) {
					return group.get(groupId, codeId);
				}
			}
		}
		return defaultValue;
	}

	public static List<Code> getGroup(String locale, String groupId) {
		if (locale == null){
			// get default locale as en_US
			locale = "en_US";
		}
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.locale.equals(locale)) {
				if (group.containsKey(groupId)) {
					return group.get(groupId);
				}
			}
		}
		return null;
	}

	private static String makeKey(String groupId, String codeId) {
		return groupId + "." + codeId;
	}

	static class Group {
		String locale;
		Map<String, List<Code>> groups;
		Map<String, String> codes;

		Group(String locale, Map<String, List<Code>> groups) {
			this.locale = locale;
			this.groups = groups;
			this.codes = new HashMap<String, String>();

			Iterator<String> groupIds = groups.keySet().iterator();
			String groupId = null;
			List<Code> codeList = null;
			while (groupIds.hasNext()) {
				groupId = groupIds.next();
				codeList = groups.get(groupId);
				for (Code c : codeList) {
					codes.put(makeKey(groupId, c.id), c.value);
				}
			}
		}

		boolean containsKey(String groupId) {
			return groups.containsKey(groupId);
		}

		List<Code> get(String groupId) {
			return groups.get(groupId);
		}

		boolean containsKey(String groupId, String codeId) {
			return codes.containsKey(makeKey(groupId, codeId));
		}

		String get(String groupId, String codeId) {
			return codes.get(makeKey(groupId, codeId));
		}

	}

}
