package skcc.nexcore.client.application.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EnvUtils {

	private static InetAddress ia;

	public final static String getDomainDisplay() {
		String domain = getDomain();
		return domain == null ? "N/A" : domain;
	}

	public final static String getDomain() {
		return System.getProperty("nexadmin.domain");
	}

	public final static String getHostName() {
		loadInetAddress();
		if (ia != null) {
			return ia.getHostName();
		}
		return null;
	}

	public final static String getHostAddress() {
		loadInetAddress();
		if (ia != null) {
			return ia.getHostAddress();
		}
		return null;
	}

	private final static void loadInetAddress() {
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
	}
}
