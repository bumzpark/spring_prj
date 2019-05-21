package skcc.nexcore.client.application.util;

import javax.servlet.http.HttpServletRequest;

public final class SessionHelper {

	public final static String PART_ID = "PART_ID";
	public final static String USER_ID = "USER_ID";
	public final static String USER_NAME = "USER_NAME";
	public final static String LOGIN_DATE = "LOGIN_DATE";
	public final static String GROUP_ID = "GROUP_ID";

	public final static String SELECTED_LOCALE = "SELECTED_LOCALE";

	public final static String SELECTED_DOMAIN_ID = "SELECTED_DOMAIN_ID";
	public final static String SELECTED_USER_ID = "SELECTED_USER_ID";
	public final static String SELECTED_MENU = "SELECTED_MENU";
	public final static String SELECTED_VIEW = "SELECTED_VIEW";

	public final static String OLD_SELECTED_MENU = "OLD_SELECTED_MENU";
	public final static String OLD_SELECTED_VIEW = "OLD_SELECTED_VIEW";

	public final static String STATUS_MESSAGE = "status_message";
	public final static String STATUS_MESSAGE_DETAIL = "status_message_detail";

	public static Object get(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}

	public static String getString(HttpServletRequest request, String key) {
		return (String) request.getSession().getAttribute(key);
	}

	public static void set(HttpServletRequest request, String key, Object value) {
		request.getSession().setAttribute(key, value);
	}

	public static void remove(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}

	public static void setSelectedUserId(HttpServletRequest request, String selectedUserId) {
		SessionHelper.set(request, SessionHelper.SELECTED_USER_ID, selectedUserId);
	}

	public static String getSessionId(HttpServletRequest request) {
		return request.getSession().getId();
	}

}
