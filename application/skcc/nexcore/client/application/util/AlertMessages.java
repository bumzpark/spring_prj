package skcc.nexcore.client.application.util;

import java.util.HashMap;
import java.util.Map;

import skcc.nexcore.client.foundation.util.UniqueKey;

public final class AlertMessages {

	private static Map<String, String> messages = new HashMap<String, String>();

	public final static String put(String message) {
		String key = UniqueKey.getRandomKey();
		messages.put(key, message);
		return key;
	}

	public final static String remove(String key) {
		if (messages.containsKey(key)) {
			return messages.remove(key);
		}
		return null;
	}
	
}
