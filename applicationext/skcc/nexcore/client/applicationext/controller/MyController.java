package skcc.nexcore.client.applicationext.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.GroupVO;
import skcc.nexcore.client.applicationext.entity.UserVO;
import skcc.nexcore.client.applicationext.service.GroupService;
import skcc.nexcore.client.applicationext.service.UserService;
import skcc.nexcore.client.foundation.util.SecurityUtils;

public class MyController extends BaseController {

	private UserService userService;
	private GroupService groupService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public ModelAndView myInfoView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String userId = SessionHelper.getString(request, SessionHelper.USER_ID);

		UserVO user_info = userService.select(userId);
		List<GroupVO> group_list = groupService.selectAll();

		ModelAndView mav = new ModelAndView("forward:/system/admin/myInfo.jsp");
		mav.addObject("user_info", user_info);
		mav.addObject("group_list", group_list);
		return mav;
	}

	public ModelAndView updateMyInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		UserVO entity = toUserVO(request);
		entity.userId = SessionHelper.getString(request, SessionHelper.USER_ID);
		int row = userService.updateMyInfo(entity);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/myInfoView.do");

		if (row > 0) {
			String currentPassword = request.getParameter("current_password");
			String newPassword = request.getParameter("new_password");
			if (currentPassword != null && currentPassword.trim().length() > 0) {
				entity = userService.select(entity.userId);
				if (SecurityUtils.isSameString(currentPassword, entity.password)) {
					row = userService.updatePassword(entity.userId, SecurityUtils.getMD5String(newPassword), "false");
					if (row > 0) {
						setStatusMessage(mav, false, "사용자[" + entity.userId + "] 정보 수정을 성공 하였습니다.");
					} else {
						setStatusMessage(mav, true, "사용자[" + entity.userId + "] 정보 수정을 실패 하였습니다.");
					}
				} else {
					setStatusMessage(mav, true, "사용자[" + entity.userId + "] 입력된 현재 비밀번호가 일치하지 않습니다.");
				}
			}
		} else {
			setStatusMessage(mav, true, "사용자[" + entity.userId + "] 정보 수정을 실패 하였습니다.");
		}

		return mav;
	}

	private UserVO toUserVO(HttpServletRequest request) {
		UserVO entity = new UserVO();
		entity.userId = request.getParameter("user_id");
		entity.userName = request.getParameter("user_name");
		entity.email = request.getParameter("email");
		return entity;
	}

}
