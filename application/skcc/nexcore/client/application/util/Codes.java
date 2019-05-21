package skcc.nexcore.client.application.util;

import java.util.List;

import skcc.nexcore.client.application.configuration.cache.CodeConfigurationCache;

public class Codes {

	public static String get(String groupId, String codeId, String defaultValue) {
		String locale = Locales.getLocale();
		return CodeConfigurationCache.get(locale, groupId, codeId, defaultValue);
	}

	public static List<Code> getGroup(String groupId) {
		String locale = Locales.getLocale();
		return CodeConfigurationCache.getGroup(locale, groupId);
	}

}
