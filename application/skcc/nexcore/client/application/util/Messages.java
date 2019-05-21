package skcc.nexcore.client.application.util;

import java.text.MessageFormat;

import skcc.nexcore.client.application.configuration.cache.MessageConfigurationCache;

public class Messages {

	public static String get(String id, Object... arguments) {
		String locale = Locales.getLocale();
		String message = MessageConfigurationCache.get(locale, id);
		if (message == null) {
			return id;
		}
		return MessageFormat.format(message, arguments);
	}
	
	public static void main(String[] args){
		System.out.println(MessageFormat.format("''{0}''를 선택하세요.", "WAS"));
	}

}
