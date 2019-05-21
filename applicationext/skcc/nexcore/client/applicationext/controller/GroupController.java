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
import skcc.nexcore.client.applicationext.entity.GroupLocaleVO;
import skcc.nexcore.client.applicationext.entity.MenuGroupMapVO;
import skcc.nexcore.client.applicationext.entity.GroupVO;
import skcc.nexcore.client.applicationext.entity.MenuVO;
import skcc.nexcore.client.applicationext.service.GroupLocaleService;
import skcc.nexcore.client.applicationext.service.GroupService;
import skcc.nexcore.client.applicationext.service.MenuService;

public class GroupController extends BaseController {

	private GroupService groupService;
	private GroupLocaleService groupLocaleService;
	private MenuService menuService;

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * @param groupLocaleService
	 *            the groupLocaleService to set
	 */
	public void setGroupLocaleService(GroupLocaleService groupLocaleService) {
		this.groupLocaleService = groupLocaleService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		List<GroupVO> group_list = groupService.selectAll();
		ModelAndView mav = new ModelAndView("forward:/system/admin/groupList.jsp");
		mav.addObject("group_list", group_list);
		return mav;
	}

	public ModelAndView insertView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		ModelAndView mav = new ModelAndView("forward:/system/admin/groupInsert.jsp");
		return mav;
	}

	public ModelAndView updateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String groupId = request.getParameter("group_id");
		GroupVO group_info = groupService.select(groupId);
		List<GroupLocaleVO> group_locale_list = groupLocaleService.selectAll(groupId);

		ModelAndView mav = new ModelAndView("forward:/system/admin/groupUpdate.jsp");
		mav.addObject("group_info", group_info);
		mav.addObject("group_locale_list", group_locale_list);
		return mav;
	}

	public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		GroupVO entity = toGroupVO(request);
		List<GroupLocaleVO> groupLocaleList = toGroupLocaleVO(request);

		int row = groupService.insert(entity);
		if (row > 0) {
			groupLocaleService.insert(groupLocaleList);
		}

		ModelAndView mav = new ModelAndView("redirect:/system/admin/groupList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "add.success" : "add.fail", row));
		return mav;
	}

	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		GroupVO entity = toGroupVO(request);
		List<GroupLocaleVO> groupLocaleList = toGroupLocaleVO(request);

		int row = groupService.update(entity);
		if (row > 0) {
			groupLocaleService.delete(new String[] { entity.groupId });
			groupLocaleService.insert(groupLocaleList);
		}

		ModelAndView mav = new ModelAndView("redirect:/system/admin/groupList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] idList = request.getParameterValues("id_list");
		int row = groupService.delete(idList);
		groupLocaleService.delete(idList);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/groupList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "remove.success" : "remove.fail", row));
		return mav;
	}

	public ModelAndView duplicateCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String menuId = request.getParameter("menu_id");
		GroupVO entity = groupService.select(menuId);
		boolean duplicated = false;
		if (entity != null) {
			duplicated = true;
		}
		ModelAndView mav = new ModelAndView("redirect:/system/common/duplicateCheck.do");
		mav.addObject("duplicated", "" + duplicated);
		return mav;
	}

	public ModelAndView menuMappingView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String groupId = request.getParameter("group_id");
		GroupVO group_info = groupService.select(groupId);
		List<GroupVO> group_list = groupService.selectAll();
		List<MenuVO> menu_list = menuService.selectAll();

		List<MenuGroupMapVO> authorized_list = groupService.selectGroupMenuMapping(groupId);

		List<String> select_list = new ArrayList<String>();
		List<String> insert_list = new ArrayList<String>();
		List<String> update_list = new ArrayList<String>();
		List<String> delete_list = new ArrayList<String>();
		List<String> server_list = new ArrayList<String>();

		if (authorized_list != null) {
			for (MenuGroupMapVO entity : authorized_list) {
				if ("true".equals(entity.selectAction)) {
					select_list.add(entity.menuId);
				}
				if ("true".equals(entity.insertAction)) {
					insert_list.add(entity.menuId);
				}
				if ("true".equals(entity.updateAction)) {
					update_list.add(entity.menuId);
				}
				if ("true".equals(entity.deleteAction)) {
					delete_list.add(entity.menuId);
				}
				if ("true".equals(entity.serverAction)) {
					server_list.add(entity.menuId);
				}
			}
		}

		ModelAndView mav = new ModelAndView("forward:/system/admin/groupMenuMapping.jsp");
		mav.addObject("group_info", group_info);
		mav.addObject("group_list", group_list);
		mav.addObject("menu_list", menu_list);
		mav.addObject("select_list", select_list);
		mav.addObject("insert_list", insert_list);
		mav.addObject("update_list", update_list);
		mav.addObject("delete_list", delete_list);
		mav.addObject("server_list", server_list);
		return mav;
	}

	public ModelAndView menuMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String groupId = request.getParameter("group_id");

		String[] select_array = request.getParameterValues("select_list");
		String[] insert_array = request.getParameterValues("insert_list");
		String[] update_array = request.getParameterValues("update_list");
		String[] delete_array = request.getParameterValues("delete_list");
		String[] server_array = request.getParameterValues("server_list");

		List<String> id_list = new ArrayList<String>();
		addList(id_list, select_array);
		addList(id_list, insert_array);
		addList(id_list, update_array);
		addList(id_list, delete_array);
		addList(id_list, server_array);

		List<String> select_list = new ArrayList<String>();
		List<String> insert_list = new ArrayList<String>();
		List<String> update_list = new ArrayList<String>();
		List<String> delete_list = new ArrayList<String>();
		List<String> server_list = new ArrayList<String>();
		addList(select_list, select_array);
		addList(insert_list, insert_array);
		addList(update_list, update_array);
		addList(delete_list, delete_array);
		addList(server_list, server_array);

		List<MenuGroupMapVO> list = new ArrayList<MenuGroupMapVO>(id_list.size());
		MenuGroupMapVO entity = null;
		for (String menuId : id_list) {
			entity = new MenuGroupMapVO();
			entity.groupId = groupId;
			entity.menuId = menuId;

			entity.selectAction = select_list.contains(menuId) ? "true" : "false";
			entity.insertAction = insert_list.contains(menuId) ? "true" : "false";
			entity.updateAction = update_list.contains(menuId) ? "true" : "false";
			entity.deleteAction = delete_list.contains(menuId) ? "true" : "false";
			entity.serverAction = server_list.contains(menuId) ? "true" : "false";

			list.add(entity);
		}

		int row = groupService.insertMenuGroupMapping(groupId, list);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/groupMenuMappingView.do");
		mav.addObject("group_id", groupId);
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	private void addList(List<String> list, String[] array) {
		if (array != null) {
			for (String str : array) {
				if (!list.contains(str)) {
					list.add(str);
				}
			}
		}
	}

	private GroupVO toGroupVO(HttpServletRequest request) {
		GroupVO entity = new GroupVO();
		entity.groupId = request.getParameter("group_id");
		entity.groupName = request.getParameter("group_name");
		entity.groupStatus = request.getParameter("group_status");
		entity.groupDesc = request.getParameter("group_desc");
		return entity;
	}

	private List<GroupLocaleVO> toGroupLocaleVO(HttpServletRequest request) {
		List<GroupLocaleVO> list = new ArrayList<GroupLocaleVO>();

		String groupId = request.getParameter("group_id");
		Enumeration parameterNames = request.getParameterNames();
		String parameterName = null;
		while (parameterNames.hasMoreElements()) {
			parameterName = (String) parameterNames.nextElement();
			if (parameterName.startsWith("group_name_")) {
				String locale = parameterName.substring("group_name_".length());
				String groupName = request.getParameter(parameterName);
				String groupDesc = request.getParameter("group_desc_" + locale);

				GroupLocaleVO entity = new GroupLocaleVO();
				entity.groupId = groupId;
				entity.locale = locale;
				entity.groupName = groupName;
				entity.groupDesc = groupDesc;
				list.add(entity);
			}
		}
		return list;
	}
}
