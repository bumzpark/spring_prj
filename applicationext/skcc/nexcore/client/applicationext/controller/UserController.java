package skcc.nexcore.client.applicationext.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.Labels;
import skcc.nexcore.client.application.util.Messages;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.applicationext.document.UserExcelBuilder;
import skcc.nexcore.client.applicationext.document.UserPdfBuilder;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.GroupVO;
import skcc.nexcore.client.applicationext.entity.UserVO;
import skcc.nexcore.client.applicationext.service.GroupService;
import skcc.nexcore.client.applicationext.service.UserService;
import skcc.nexcore.client.foundation.mail.MailAddress;
import skcc.nexcore.client.foundation.mail.MailMessage;
import skcc.nexcore.client.foundation.util.SecurityUtils;
import skcc.nexcore.client.foundation.util.UniqueKey;

public class UserController extends BaseController {

	private UserService userService;
	private GroupService groupService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		// Map<String, GroupVO> group_map = (Map<String,
		// GroupVO>)request.getAttribute("group_map");

		List<UserVO> user_list = userService.selectAll();
		List<GroupVO> group_list = groupService.selectAll();

		Map<String, GroupVO> group_map = new HashMap<String, GroupVO>();
		if (group_list != null) {
			for (GroupVO o : group_list) {
				group_map.put(o.groupId, o);
			}
		}

		ModelAndView mav = new ModelAndView("forward:/system/admin/userList.jsp");
		mav.addObject("user_list", user_list);
		mav.addObject("user_status_list", DefaultConstant.USER_STATUS_LIST);
		mav.addObject("group_map", group_map);
		return mav;
	}

	public ModelAndView insertView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		List<GroupVO> group_list = groupService.selectAll();

		ModelAndView mav = new ModelAndView("forward:/system/admin/userInsert.jsp");
		mav.addObject("group_list", group_list);
		mav.addObject("user_status_list", DefaultConstant.USER_STATUS_LIST);
		return mav;
	}

	public ModelAndView updateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String userId = request.getParameter("user_id");

		UserVO user_info = userService.select(userId);

		List<GroupVO> group_list = groupService.selectAll();

		ModelAndView mav = new ModelAndView("forward:/system/admin/userUpdate.jsp");
		mav.addObject("user_info", user_info);
		mav.addObject("group_list", group_list);
		mav.addObject("user_status_list", DefaultConstant.USER_STATUS_LIST);
		return mav;
	}

	public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		UserVO entity = toUserVO(request);
		entity.password = SecurityUtils.getMD5String(entity.password);
		entity.status = DefaultConstant.STATUS_NEW_HIRE;
		// entity.creUserId = SessionHelper.getString(request,
		// SessionHelper.USER_ID);
		// entity.creDate = new Timestamp(System.currentTimeMillis());

		int row = userService.insert(entity);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/userList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "add.success" : "add.fail", row));
		return mav;
	}

	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		UserVO entity = toUserVO(request);
		// entity.domainUser = "false";

		int row = userService.update(entity);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/userList.do");
		if (row > 0) {
			String newPassword = request.getParameter("password");
			if (newPassword != null && newPassword.trim().length() > 0) {
				row = userService.updatePassword(entity.userId, SecurityUtils.getMD5String(newPassword), "true");
			}
		}
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String logingId = SessionHelper.getString(request, SessionHelper.USER_ID);
		String userId = request.getParameter("user_id");

		String password = UniqueKey.getKey();
		int row = userService.updatePassword(userId, SecurityUtils.getMD5String(password), "true");

		if (row > 0) {
			UserVO entity = userService.select(userId);
			String emailTo = entity.email;

			if (emailTo == null || emailTo.trim().length() < 1) {
				throw new Exception("E-Mail is null. Can't reset password.");
			}

			UserVO loginEntity = userService.select(logingId);
			String emailFrom = loginEntity.email;

			MailMessage mm = new MailMessage();
			mm.setSubject(Labels.getSiteTitle() + " " + entity.userName + " login password reset.");
			mm.setFrom(new MailAddress(emailFrom));
			mm.setTo(new MailAddress[] { new MailAddress(emailTo) });
			mm.setBody("Login password reset. \nTemporary login password is [" + password + "].");

			sendMail(mm);
		}

		ModelAndView mav = new ModelAndView("redirect:/system/admin/userList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "reset.success" : "reset.fail", row));
		return mav;
	}

	public ModelAndView updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String userId = request.getParameter("user_id");
		String status = request.getParameter("status");

		int row = userService.updateStatus(userId, status);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/userList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "modify.success" : "modify.fail", row));
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] userIdList = request.getParameterValues("id_list");
		int row = userService.delete(userIdList);
		ModelAndView mav = new ModelAndView("redirect:/system/admin/userList.do");
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "remove.success" : "remove.fail", row));
		return mav;
	}

	public ModelAndView duplicateCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String userId = request.getParameter("user_id");
		UserVO user_info = userService.select(userId);
		boolean duplicated = false;
		if (user_info != null) {
			duplicated = true;
		}
		ModelAndView mav = new ModelAndView("redirect:/system/common/duplicateCheck.do");
		mav.addObject("duplicated", "" + duplicated);
		return mav;
	}

	public ModelAndView excel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserExcelBuilder builder = new UserExcelBuilder(response, "user-list.xls", userService.selectAll());
		builder.build();
		return null;
	}

	public ModelAndView pdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserPdfBuilder builder = new UserPdfBuilder(response, "user-list.pdf", SessionHelper.getString(request, SessionHelper.USER_ID), userService.selectAll());
		builder.build();
		return null;
	}

	private UserVO toUserVO(HttpServletRequest request) {
		UserVO entity = new UserVO();
		entity.userId = request.getParameter("user_id");
		entity.userName = request.getParameter("user_name");
		entity.email = request.getParameter("email");
		entity.groupId = request.getParameter("group_id");
		entity.status = request.getParameter("status");
		entity.password = request.getParameter("password");
		return entity;
	}

}
