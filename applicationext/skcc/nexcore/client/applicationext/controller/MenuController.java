package skcc.nexcore.client.applicationext.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.Messages;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.MenuLocaleVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;
import skcc.nexcore.client.applicationext.service.MenuLocaleService;
import skcc.nexcore.client.applicationext.service.MenuService;

public class MenuController extends BaseController {

	private MenuService menuService;
	private MenuLocaleService menuLocaleService;

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	/**
	 * @param menuLocaleService
	 *            the menuLocaleService to set
	 */
	public void setMenuLocaleService(MenuLocaleService menuLocaleService) {
		this.menuLocaleService = menuLocaleService;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		List<MenuVO> menu_list = menuService.selectAll();
		ModelAndView mav = new ModelAndView("forward:/system/admin/menuList.jsp");
		mav.addObject("menu_list", menu_list);
		return mav;
	}

	public ModelAndView insertView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		List<MenuVO> menu_list = menuService.selectDirAll();
		ModelAndView mav = new ModelAndView("forward:/system/admin/menuInsert.jsp");
		mav.addObject("menu_list", menu_list);
		return mav;
	}

	public ModelAndView updateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String menuId = request.getParameter("menu_id");
		MenuVO menu_info = menuService.select(menuId);
		List<MenuVO> menu_list = menuService.selectDirAll();
		List<MenuLocaleVO> menu_locale_list = menuLocaleService.selectAll(menuId);

		ModelAndView mav = new ModelAndView("forward:/system/admin/menuUpdate.jsp");
		mav.addObject("menu_info", menu_info);
		mav.addObject("menu_list", menu_list);
		mav.addObject("menu_locale_list", menu_locale_list);
		return mav;
	}

	public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		MenuVO entity = toMenuVO(request);
		List<MenuLocaleVO> menuLocaleList = toMenuLocaleVO(request);

		if (entity.parentId != null && entity.parentId.length() > 0) {
			MenuVO parent = menuService.select(entity.parentId);
			entity.menuLevel = parent.menuLevel + 1;
		}

		int row = menuService.insert(entity);
		if (row > 0) {
			menuLocaleService.insert(menuLocaleList);
		}

		ModelAndView mav = new ModelAndView("redirect:/system/admin/menuList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "add.success" : "add.fail", row));
		return mav;
	}

	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		MenuVO entity = toMenuVO(request);
		List<MenuLocaleVO> menuLocaleList = toMenuLocaleVO(request);

		if (entity.parentId != null && entity.parentId.length() > 0) {
			MenuVO parent = menuService.select(entity.parentId);
			entity.menuLevel = parent.menuLevel + 1;
		}

		int row = menuService.update(entity);
		if (row > 0) {
			menuLocaleService.delete(new String[] { entity.menuId });
			menuLocaleService.insert(menuLocaleList);
		}

		ModelAndView mav = new ModelAndView("redirect:/system/admin/menuList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	public ModelAndView updateOrderNo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] menu_id_list = request.getParameterValues("menu_id_list");
		String[] order_no_list = request.getParameterValues("order_no_list");

		int row = menuService.updateOrderNo(menu_id_list, order_no_list);
		ModelAndView mav = new ModelAndView("redirect:/system/admin/menuList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] idList = request.getParameterValues("id_list");
		int row = menuService.delete(idList);
		menuLocaleService.delete(idList);
		ModelAndView mav = new ModelAndView("redirect:/system/admin/menuList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "remove.success" : "remove.fail", row));
		return mav;
	}

	public ModelAndView duplicateCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String menuId = request.getParameter("menu_id");
		MenuVO entity = menuService.select(menuId);
		boolean duplicated = false;
		if (entity != null) {
			duplicated = true;
		}
		ModelAndView mav = new ModelAndView("redirect:/system/common/duplicateCheck.do");
		mav.addObject("duplicated", "" + duplicated);
		return mav;
	}

	private MenuVO toMenuVO(HttpServletRequest request) {
		MenuVO entity = new MenuVO();
		entity.menuId = request.getParameter("menu_id");
		entity.menuName = request.getParameter("menu_name");
		entity.menuUrl = request.getParameter("menu_url");
		entity.parentId = request.getParameter("parent_id");
		entity.menuDesc = request.getParameter("menu_desc");
		return entity;
	}

	private List<MenuLocaleVO> toMenuLocaleVO(HttpServletRequest request) {
		List<MenuLocaleVO> list = new ArrayList<MenuLocaleVO>();

		String menuId = request.getParameter("menu_id");
		Enumeration parameterNames = request.getParameterNames();
		String parameterName = null;
		while (parameterNames.hasMoreElements()) {
			parameterName = (String) parameterNames.nextElement();
			if (parameterName.startsWith("menu_name_")) {
				String locale = parameterName.substring("menu_name_".length());
				String menuName = request.getParameter(parameterName);
				String menuDesc = request.getParameter("menu_desc_" + locale);

				MenuLocaleVO entity = new MenuLocaleVO();
				entity.menuId = menuId;
				entity.locale = locale;
				entity.menuName = menuName;
				entity.menuDesc = menuDesc;
				list.add(entity);
			}
		}
		return list;
	}
}
