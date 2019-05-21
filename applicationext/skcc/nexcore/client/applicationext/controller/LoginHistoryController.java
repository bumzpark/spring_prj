package skcc.nexcore.client.applicationext.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.Messages;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.LoginHistoryVO;
import skcc.nexcore.client.applicationext.entity.PageNavigation;
import skcc.nexcore.client.applicationext.service.LoginHistoryService;
import skcc.nexcore.client.foundation.util.StringUtils;

public class LoginHistoryController extends BaseController {

	private LoginHistoryService loginHistoryService;

	public void setLoginHistoryService(LoginHistoryService loginHistoryService) {
		this.loginHistoryService = loginHistoryService;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		PageNavigation pageNavigation = PageNavigation.request(request);
		if (pageNavigation.isRequestFirstPage || pageNavigation.isRequestLastPage) {
			pageNavigation.setTotalCount(loginHistoryService.selectTotalCount());
		}
		List<LoginHistoryVO> entity_list = loginHistoryService.selectAll(pageNavigation);

		ModelAndView mav = new ModelAndView("forward:/system/admin/loginHistory.jsp");
		mav.addObject("entity_list", entity_list);
		PageNavigation.response(mav, pageNavigation, entity_list);
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] idList = request.getParameterValues("id_list");
		List<LoginHistoryVO> list = null;
		if (idList != null) {
			list = new ArrayList<LoginHistoryVO>();

			List<String> array = null;
			LoginHistoryVO entity = null;
			for (String str : idList) {
				array = StringUtils.stringToList(str, "^");
				entity = new LoginHistoryVO();
				entity.loginDate = array.get(0);
				entity.userId = array.get(1);
				list.add(entity);
			}
		}

		int row = loginHistoryService.delete(list);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/loginHistory.do");
		mav.addObject("row_per_page", request.getParameter("row_per_page"));
		mav.addObject("request_page", request.getParameter("request_page"));
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "remove.success.count" : "remove.fail.count", row));
		return mav;
	}

}
