package skcc.nexcore.client.application.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import skcc.nexcore.client.application.configuration.cache.LocaleConfigurationCache;

public final class Locales {

	private static ThreadLocal<LocaleObject> locales = new ThreadLocal<LocaleObject>();

	public static void setLocale(HttpServletRequest request) {
		String locale = SessionHelper.getString(request, SessionHelper.SELECTED_LOCALE);
		LocaleObject lo = null;
		if (locale == null || locale.trim().length() < 1) {
			lo = LocaleConfigurationCache.getDefault();
		} else {
			lo = LocaleConfigurationCache.get(locale);
		}
		locales.set(lo);
	}

	public static void clearLocale() {
		//		locales.remove();
	}

	public static List<LocaleObject> getAll() {
		return LocaleConfigurationCache.getAll();
	}

	public static String getLocale() {
		LocaleObject lo = locales.get();
		if (lo == null) {
			return null;
		}
		return lo.id;
	}

	public static String getDateFormat(HttpServletRequest request) {
		String locale = SessionHelper.getString(request, SessionHelper.SELECTED_LOCALE);
		LocaleObject lo = null;
		if (locale == null || locale.trim().length() < 1) {
			lo = LocaleConfigurationCache.getDefault();
		} else {
			lo = LocaleConfigurationCache.get(locale);
		}

		if (lo == null) {
			return "MM/dd/yyyy";
		}
		return lo.dateFormat;
	}

}
