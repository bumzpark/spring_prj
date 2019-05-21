package skcc.nexcore.client.application.configuration.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import skcc.nexcore.client.application.util.Data;

public class DataConfigurationCache {

	private static Map<String, Group> cache = new HashMap<String, Group>();

	public static void add(String filename, String server, Map<String, List<Data>> groups) {
		cache.remove(filename);
		cache.put(filename, new Group(server, groups));
	}

	public static String get(String server, String groupId, String dataId, String defaultValue) {
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.server.equals(server)) {
				if (group.containsKey(groupId, dataId)) {
					return group.get(groupId, dataId);
				}
			}
		}
		return defaultValue;
	}

	public static List<Data> getGroup(String server, String groupId) {
		Iterator<Group> values = cache.values().iterator();
		while (values.hasNext()) {
			Group group = values.next();
			if (group.server.equals(server)) {
				if (group.containsKey(groupId)) {
					return group.get(groupId);
				}
			}
		}
		return null;
	}

	private static String makeKey(String groupId, String dataId) {
		return groupId + "." + dataId;
	}

	static class Group {
		String server;
		Map<String, List<Data>> groups;
		Map<String, String> datas;

		Group(String server, Map<String, List<Data>> groups) {
			this.server = server;
			this.groups = groups;
			this.datas = new HashMap<String, String>();

			Iterator<String> groupIds = groups.keySet().iterator();
			String groupId = null;
			List<Data> dataList = null;
			while (groupIds.hasNext()) {
				groupId = groupIds.next();
				dataList = groups.get(groupId);
				for (Data c : dataList) {
					datas.put(makeKey(groupId, c.id), c.value);
				}
			}
		}

		boolean containsKey(String groupId) {
			return groups.containsKey(groupId);
		}

		List<Data> get(String groupId) {
			return groups.get(groupId);
		}

		boolean containsKey(String groupId, String dataId) {
			return datas.containsKey(makeKey(groupId, dataId));
		}

		String get(String groupId, String dataId) {
			return datas.get(makeKey(groupId, dataId));
		}

	}

}
