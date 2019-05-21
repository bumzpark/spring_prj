package skcc.nexcore.client.applicationext.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.foundation.util.UniqueKey;

public class AuthCheckInterceptor extends HandlerInterceptorAdapter {

	public final static String INTERNAL_SESSION_KEY_NAME = "INTERNAL_SESSION_KEY";
	public final static String INTERNAL_SESSION_KEY_VALUE = UniqueKey.getRandomKey();

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 세션 체크하지 않을 대상으로 설정되어 있는 목록을 이 변수에 설정
	 */
	private List<String> skipList = null;

	private List<String> initList = null;

	private String sessionoutPage;

	public void setSkipList(List<String> skipList) {
		this.skipList = skipList;
	}

	public void setInitList(List<String> initList) {
		this.initList = initList;
	}

	public void setSessionoutPage(String sessionoutPage) {
		this.sessionoutPage = sessionoutPage;
	}

	/**
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter 클래스에서
	 * 정의하는 preHandler 메소드를 override 한 메소드. 요청을 처리하기 전에 preHandler 메소드를 우선 수행
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		initSession(request);

		// 세션이 연결되어 있을 때는 요청 진행
		if (SessionHelper.get(request, SessionHelper.USER_ID) != null) {
			Locales.setLocale(request);
			return true;
		}
		// 세션이 연결되어 있지 않을 경우 처리 로직 결정
		else {
			if (isAvailableActionWithoutLogin(request.getServletPath())) {
				Locales.setLocale(request);
				return true;
			} else if (isInternalSessionKey(request)) {
				return true;
			} else {
				response.sendRedirect(request.getContextPath() + sessionoutPage);
				return false;
			}
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
		Locales.clearLocale();
	}

	private void initSession(HttpServletRequest request) {
		if (isInitUrl(request.getServletPath())) {
			SessionHelper.set(request, SessionHelper.SELECTED_MENU, null);
			SessionHelper.set(request, SessionHelper.SELECTED_VIEW, null);
		}
	}

	// URL이 세션 체크가 필요 없는 것인지 확인
	private boolean isAvailableActionWithoutLogin(String url) {
		if (skipList != null) {
			for (String entity : skipList) {
				if (entity.equals(url)) {
					return true;
				}
			}
		}
		if (sessionoutPage != null) {
			return sessionoutPage.endsWith(url);
		}
		return false;
	}

	private boolean isInitUrl(String url) {
		if (initList != null) {
			for (String entity : initList) {
				if (entity.equals(url)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInternalSessionKey(HttpServletRequest request) {
		String sessionKey = request.getParameter(INTERNAL_SESSION_KEY_NAME);
		if (INTERNAL_SESSION_KEY_VALUE.equals(sessionKey)) {
			return true;
		}
		return false;
	}

}