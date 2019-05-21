package skcc.nexcore.client.application.util;

import java.util.List;

import skcc.nexcore.client.application.configuration.cache.DataConfigurationCache;

public class Datas {

	private static String getServer(){
		String server = System.getProperty("SERVER_ENV");
		if (server == null){
			server = "DEV";
		}
		return server;
	}
	
	public static String get(String groupId, String codeId, String defaultValue) {
		return DataConfigurationCache.get(getServer(), groupId, codeId, defaultValue);
	}

	public static List<Data> getGroup(String groupId) {
		return DataConfigurationCache.getGroup(getServer(), groupId);
	}

}
