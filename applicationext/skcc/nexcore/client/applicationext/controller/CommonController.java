package skcc.nexcore.client.applicationext.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.EnvUtils;
import skcc.nexcore.client.application.util.Locales;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.application.web.HttpParameterData;
import skcc.nexcore.client.applicationext.entity.GroupVO;
import skcc.nexcore.client.applicationext.entity.MenuSortedVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;
import skcc.nexcore.client.applicationext.service.GroupService;
import skcc.nexcore.client.applicationext.service.MenuService;
import skcc.nexcore.client.applicationext.service.UserService;

public class CommonController extends BaseController {

	private MenuService menuService;
	private UserService userService;
	private GroupService groupService;

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param groupService
	 *            the groupService to set
	 */
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public ModelAndView mainDecorator(HttpServletRequest request, HttpServletResponse response, HttpParameterData requestData) throws Exception {
		String groupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		String notitle = requestData.getParameter("notitle");
		String nomenu = requestData.getParameter("nomenu");

		// String selectedDomainId = SessionHelper.getString(request,
		// SessionHelper.SELECTED_DOMAIN_ID);

		List<MenuVO> memu_list = menuService.selectGroupTopAll(groupId);
		String group_id = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		GroupVO group_info = groupService.select(group_id);
		
		// List<DomainVO> domain_list = null;
		// if
		// (DefaultConstant.isSystemOrFunctionAdminLevel(SessionHelper.getInt(request,
		// SessionHelper.GROUP_LEVEL))) {
		// domain_list =
		// domainService.selectStatusAll(DefaultConstant.STATUS_ACTIVE);
		// } else {
		// String ownerId = SessionHelper.getString(request,
		// SessionHelper.USER_ID);
		// domain_list = domainService.selectStatusAll(ownerId,
		// DefaultConstant.STATUS_ACTIVE);
		// }
		// DefaultConstant.removeSystemDomainId(domain_list);

		// List<UserVO> user_list = null;
		// if (selectedDomainId != null) {
		// user_list = userService.selectDomainStatusAll(selectedDomainId,
		// "true", DefaultConstant.STATUS_ACTIVE);
		// }

		ModelAndView mav = new ModelAndView("forward:/system/decorators/main.jsp");
		mav.addObject("menu_list", memu_list);
		// mav.addObject("domain_list", domain_list);
		// mav.addObject("user_list", user_list);
		mav.addObject("notitle", notitle);
		mav.addObject("nomenu", nomenu);
		mav.addObject("hostName", EnvUtils.getHostName());
		mav.addObject("hostAddress", EnvUtils.getHostAddress());
		mav.addObject("group_info", group_info);
		return mav;
	}

	public ModelAndView bottomDecorator(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("forward:/system/decorators/bottom.jsp");
	}

	public ModelAndView menuDecorator(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String groupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		String menuId = SessionHelper.getString(request, SessionHelper.SELECTED_MENU);
		String viewId = SessionHelper.getString(request, SessionHelper.SELECTED_VIEW);
		// String selectedDomainId = SessionHelper.getString(request,
		// SessionHelper.SELECTED_DOMAIN_ID);

		List<MenuVO> menu_list = null;
		MenuSortedVO menuTopEntity = menuService.selectGroupSubAll(menuId, groupId);
		if (menuTopEntity != null) {
			menu_list = menuTopEntity.getChildAll();
		}

		// List<DomainVO> domain_list = null;
		// if
		// (DefaultConstant.isSystemOrFunctionAdminLevel(SessionHelper.getInt(request,
		// SessionHelper.GROUP_LEVEL))) {
		// domain_list =
		// domainService.selectStatusAll(DefaultConstant.STATUS_ACTIVE);
		// } else {
		// String ownerId = SessionHelper.getString(request,
		// SessionHelper.USER_ID);
		// domain_list = domainService.selectStatusAll(ownerId,
		// DefaultConstant.STATUS_ACTIVE);
		// }
		// DefaultConstant.removeSystemDomainId(domain_list);
		//
		// List<UserVO> user_list = null;
		// if (selectedDomainId != null) {
		// user_list = userService.selectDomainStatusAll(selectedDomainId,
		// "true", DefaultConstant.STATUS_ACTIVE);
		// }

		ModelAndView mav = new ModelAndView("forward:/system/decorators/menu.jsp");
		mav.addObject("menu_list", menu_list);
		mav.addObject("menu_id", viewId);
		// mav.addObject("domain_list", domain_list);
		// mav.addObject("user_list", user_list);
		return mav;
	}

	public ModelAndView pageTitle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewId = SessionHelper.getString(request, SessionHelper.SELECTED_VIEW);
		ModelAndView mav = new ModelAndView("forward:/system/decorators/pageTitle.jsp");
		List<String> names = menuService.selectMenuName(viewId);
		String menuName = "";
		if (names != null) {
			for (String name : names) {
				menuName += "> " + name + " ";
			}
		}
		mav.addObject("view_name", menuName == null ? "" : menuName);
		return mav;
	}

	public ModelAndView navigate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = SessionHelper.getString(request, SessionHelper.USER_ID);
		String groupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		String menu = request.getParameter("menu");

		List<MenuVO> menu_list = null;
		MenuSortedVO menuTopEntity = menuService.selectGroupSubAll(menu, groupId);
		MenuVO entity = null;
		if (menuTopEntity != null) {
			entity = menuTopEntity.getMe();
			menu_list = menuTopEntity.getChildAll();
		}

		if (entity == null || entity.menuUrl == null || entity.menuUrl.trim().length() < 1) {
			if (menu_list != null) {
				for (Iterator<MenuVO> iter = menu_list.iterator(); iter.hasNext();) {
					entity = iter.next();
					// URL을 포함하고 있는 메뉴인지 확인
					if (entity.menuUrl != null && entity.menuUrl.trim().length() > 1) {
						break;
					}
				}
			}
		}
		SessionHelper.set(request, SessionHelper.SELECTED_MENU, menu);

		ModelAndView mav = null;
		if (entity == null || entity.menuUrl == null || entity.menuUrl.trim().length() < 1) {
			mav = new ModelAndView("redirect:/system/main.do");
		} else {
			mav = new ModelAndView("redirect:" + entity.menuUrl);
		}
		SessionHelper.set(request, SessionHelper.SELECTED_VIEW, entity == null ? "" : entity.menuId);
		return mav;
	}

	public ModelAndView navigateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String groupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		String menuId = request.getParameter("menu_id");
		String menu = request.getParameter("menu");
		boolean back = "true".equals(request.getParameter("back"));

		if (back) {
			menu = SessionHelper.getString(request, SessionHelper.OLD_SELECTED_MENU);
			menuId = SessionHelper.getString(request, SessionHelper.OLD_SELECTED_VIEW);
		} else {
			if (menuId == null || menuId.trim().length() < 1) {
				menuId = SessionHelper.getString(request, SessionHelper.SELECTED_VIEW);
			}

			SessionHelper.set(request, SessionHelper.OLD_SELECTED_MENU, SessionHelper.getString(request, SessionHelper.SELECTED_MENU));
			SessionHelper.set(request, SessionHelper.OLD_SELECTED_VIEW, SessionHelper.getString(request, SessionHelper.SELECTED_VIEW));
		}

		MenuVO entity = menuService.selectGroupMenu(groupId, menuId);

		ModelAndView mav = null;
		if (menuId == null || menuId.trim().length() < 1) {
			mav = new ModelAndView("redirect:/system/main.do");
		} else {
			if (entity == null) {
				mav = new ModelAndView("forward:/system/error/errorRole.do");
			} else {
				mav = new ModelAndView("redirect:" + entity.menuUrl);
			}
		}

		if (menu != null && menu.trim().length() > 0) {
			SessionHelper.set(request, SessionHelper.SELECTED_MENU, menu);
		}

		SessionHelper.set(request, SessionHelper.SELECTED_VIEW, entity == null ? "" : entity.menuId);
		return mav;
	}

	public ModelAndView changeLocale(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String user_locale = request.getParameter("user_locale");
		SessionHelper.set(request, SessionHelper.SELECTED_LOCALE, user_locale);
		Locales.setLocale(request);
		ModelAndView mav = new ModelAndView("redirect:/system/common/navigateView.do");
		return mav;
	}

	public ModelAndView changeDomain(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String kind = request.getParameter("kind");
		// if ("domain".equals(kind)) {
		// String domainId = request.getParameter("id");
		// SessionHelper.setSelectedDomainId(request, domainId);
		// } else if ("user".equals(kind)) {
		// String userId = request.getParameter("id");
		// SessionHelper.setSelectedUserId(request, userId);
		// }
		ModelAndView mav = new ModelAndView("redirect:/system/common/navigateView.do");
		return mav;
	}

}
