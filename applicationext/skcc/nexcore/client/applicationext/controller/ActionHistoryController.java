package skcc.nexcore.client.applicationext.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.application.util.Messages;
import skcc.nexcore.client.applicationext.entity.ActionHistoryVO;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.PageNavigation;
import skcc.nexcore.client.applicationext.service.ActionHistoryService;
import skcc.nexcore.client.foundation.util.StringUtils;

public class ActionHistoryController extends BaseController {

	private ActionHistoryService actionHistoryService;

	public void setActionHistoryService(ActionHistoryService actionHistoryService) {
		this.actionHistoryService = actionHistoryService;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		PageNavigation pageNavigation = PageNavigation.request(request);
		if (pageNavigation.isRequestFirstPage || pageNavigation.isRequestLastPage) {
			pageNavigation.setTotalCount(actionHistoryService.selectTotalCount());
		}
		List<ActionHistoryVO> entity_list = actionHistoryService.selectAll(pageNavigation);

		ModelAndView mav = new ModelAndView("forward:/system/admin/actionHistory.jsp");
		mav.addObject("entity_list", entity_list);
		PageNavigation.response(mav, pageNavigation, entity_list);
		return mav;
	}

	public ModelAndView listByUserView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		PageNavigation pageNavigation = PageNavigation.request(request);
		if (pageNavigation.isRequestFirstPage || pageNavigation.isRequestLastPage) {
			pageNavigation.setTotalCount(actionHistoryService.selectTotalCount());
		}
		List<ActionHistoryVO> entity_list = actionHistoryService.selectAll(pageNavigation);

		ModelAndView mav = new ModelAndView("forward:/system/admin/actionHistoryByUser.jsp");
		mav.addObject("entity_list", entity_list);
		PageNavigation.response(mav, pageNavigation, entity_list);
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] idList = request.getParameterValues("id_list");
		List<ActionHistoryVO> list = null;
		if (idList != null) {
			list = new ArrayList<ActionHistoryVO>();

			List<String> array = null;
			ActionHistoryVO entity = null;
			for (String str : idList) {
				array = StringUtils.stringToList(str, "^");
				entity = new ActionHistoryVO();
				entity.userId = array.get(0);
				entity.actionDate = array.get(1);
				list.add(entity);
			}
		}

		int row = actionHistoryService.delete(list);

		ModelAndView mav = new ModelAndView("redirect:/system/admin/actionHistory.do");
		mav.addObject("row_per_page", request.getParameter("row_per_page"));
		mav.addObject("request_page", request.getParameter("request_page"));
		setStatusMessage(mav, row < 1, Messages.get(row > 0 ? "remove.success.count" : "remove.fail.count", row));
		return mav;
	}

}
