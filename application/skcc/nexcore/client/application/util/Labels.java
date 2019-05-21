package skcc.nexcore.client.application.util;

import skcc.nexcore.client.application.configuration.cache.LabelConfigurationCache;

public class Labels {

	public static String getSiteKind() {
		return ("PROD".equals(System.getProperty("SERVER_ENV"))) ? Labels.get(
				"site.kind.prod", "PRODUCT") : Labels.get("site.kind.dev",
				"DEVELOP");
	}
	
	public static String getSiteTitle() {
		return "[" + getSiteKind() + "(" + EnvUtils.getHostName() + ")" + "]-" + Labels.get("site.title", "");
	}

	public static String get(String id, String defaultValue) {
		String locale = Locales.getLocale();
		return LabelConfigurationCache.get(locale, id, defaultValue);
	}

}
