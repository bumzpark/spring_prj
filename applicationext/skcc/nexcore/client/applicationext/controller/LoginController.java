package skcc.nexcore.client.applicationext.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.Codes;
import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.GroupVO;
import skcc.nexcore.client.applicationext.entity.LoginHistoryVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;
import skcc.nexcore.client.applicationext.entity.UserVO;
import skcc.nexcore.client.applicationext.service.GroupService;
import skcc.nexcore.client.applicationext.service.LoginHistoryService;
import skcc.nexcore.client.applicationext.service.MenuService;
import skcc.nexcore.client.applicationext.service.UserService;
import skcc.nexcore.client.foundation.util.DateTimeUtils;
import skcc.nexcore.client.foundation.util.SecurityUtils;

public class LoginController extends BaseController {

	private UserService userService;
	private GroupService groupService;
	private MenuService menuService;
	private LoginHistoryService loginHistoryService;

	public void setLoginHistoryService(LoginHistoryService loginHistoryService) {
		this.loginHistoryService = loginHistoryService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public ModelAndView loginView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = null;
		if (SessionHelper.get(request, SessionHelper.USER_ID) != null) {
			mav = new ModelAndView("redirect:/system/main.do");
		} else {
			String userId = request.getParameter("user_id");
			String userLocale = request.getParameter("user_locale");

			SessionHelper.set(request, SessionHelper.SELECTED_LOCALE, userLocale);
			Locales.setLocale(request);

			mav = new ModelAndView("forward:/system/index.jsp");
			mav.addObject("user_id", userId);
		}
		return mav;
	}

	public ModelAndView loginAction(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		String userId = request.getParameter("user_id");
		String userPassword = request.getParameter("user_password");
		String userLocale = request.getParameter("user_locale");

		ModelAndView mav = null;
		String authorizedCode = null;
		String returnUrl = "redirect:/system/index.do";
		boolean loginFail = true;
		GroupVO groupEntity = null;
		String sessionId = null;

		UserVO entity = userService.select(userId);
		if (entity == null) {
			authorizedCode = LoginHistoryVO.STATUS_NOTFOUND;
		} else if (!SecurityUtils.isSameString(userPassword, entity.password)) {
			authorizedCode = LoginHistoryVO.STATUS_PASSWORD_INVALID;
		} else if (DefaultConstant.STATUS_NEW_HIRE.equals(entity.status)) {
			authorizedCode = LoginHistoryVO.STATUS_NEW_HIRE;
		} else if (DefaultConstant.STATUS_INACTIVE.equals(entity.status)) {
			authorizedCode = LoginHistoryVO.STATUS_INACTIVE;
		} else if (DefaultConstant.STATUS_TERMINATED.equals(entity.status)) {
			authorizedCode = LoginHistoryVO.STATUS_TERMINATED;
		} else {
			groupEntity = groupService.select(entity.groupId);
			if (groupEntity == null) {
				authorizedCode = LoginHistoryVO.STATUS_GROUP_INVALID;
			} else if (!"true".equals(groupEntity.groupStatus)) {
				authorizedCode = LoginHistoryVO.STATUS_GROUP_DISABLED;
			} else {
				loginFail = false;
				authorizedCode = LoginHistoryVO.STATUS_ACTIVE;
				returnUrl = "redirect:/system/main.do";

				sessionId = setSession(request, userLocale, entity, groupEntity);
			}
		}

		mav = new ModelAndView(returnUrl);
		if (loginFail) {
			SessionHelper.set(request, SessionHelper.SELECTED_LOCALE, userLocale);
			Locales.setLocale(request);

			mav.addObject("user_id", userId);
			mav.addObject("user_locale", userLocale);
			mav.addObject("login_fail", "" + loginFail);
			mav.addObject("login_fail_message", Codes.get("system.login", authorizedCode, authorizedCode));
		}

		loginHistory(userId, request.getRemoteAddr(), sessionId, groupEntity, authorizedCode);
		return mav;
	}

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String domainId = SessionHelper.getString(request,
		// SessionHelper.DOMAIN_ID);
		// String currendDate =
		// DateTimeUtils.getFormatString(System.currentTimeMillis(),
		// "yyyyMMdd");

		// List<NoticeVO> notice_list =
		// noticeService.selectDisplayAll(domainId);

		SessionHelper.set(request, SessionHelper.SELECTED_MENU, "");
		SessionHelper.set(request, SessionHelper.SELECTED_VIEW, "");

		ModelAndView mav = new ModelAndView("forward:/system/main.jsp");
		// mav.addObject("notice_list", notice_list);
		return mav;
	}

	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("redirect:/system/main.do");
	}

	public ModelAndView sitemap(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String groupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);

		List<MenuVO> menu_list = menuService.selectGroupMenuAll(groupId);

		SessionHelper.set(request, SessionHelper.SELECTED_MENU, null);
		SessionHelper.set(request, SessionHelper.SELECTED_VIEW, null);

		ModelAndView mav = new ModelAndView("forward:/system/sitemap.jsp");
		mav.addObject("menu_list", menu_list);

		return mav;
	}

	public ModelAndView logoutAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate();
		ModelAndView mav = new ModelAndView("redirect:/system/index.do");
		return mav;
	}

	private String setSession(HttpServletRequest request, String locale, UserVO entity, GroupVO groupEntity) {
		SessionHelper.set(request, SessionHelper.SELECTED_LOCALE, locale);

		SessionHelper.set(request, SessionHelper.USER_ID, entity.userId);
		SessionHelper.set(request, SessionHelper.USER_NAME, entity.userName);
		SessionHelper.set(request, SessionHelper.LOGIN_DATE, DateTimeUtils.getTimestamp());
		SessionHelper.set(request, SessionHelper.GROUP_ID, entity.groupId);
		//		request.getSession().setMaxInactiveInterval(3*60);
		// SessionHelper.set(request, SessionHelper.DOMAIN_ID, entity.domainId);
		// SessionHelper.set(request, SessionHelper.DOMAIN_NAME,
		// domainEntity.domainName);
		// SessionHelper.set(request, SessionHelper.GROUP_LEVEL,
		// groupEntity.groupLevel);
		// if (!DefaultConstant.SYSTEM_DOMAIN_ID.equals(entity.domainId)) {
		// SessionHelper.setSelectedDomainId(request, entity.domainId);
		// }

		return SessionHelper.getSessionId(request);
	}

	private void loginHistory(String userId, String clientIp, String sessionId, GroupVO groupEntity, String authorizedCode) {
		if (logger.isInfoEnabled()) {
			logger.info("try login : " + userId + "@" + clientIp + ", result : " + authorizedCode);
		}

		LoginHistoryVO entity = new LoginHistoryVO();
		// entity.historyId = UniqueKey.getKey();
		entity.userId = userId;
		entity.sessionId = sessionId;
		entity.clientHostAddr = clientIp;
		entity.loginStatus = authorizedCode;
		if (groupEntity != null) {
			entity.groupId = groupEntity.groupId;
		}
		entity.loginDate = DateTimeUtils.getFormatString(new Timestamp(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");

		loginHistoryService.insert(entity);
	}

}
