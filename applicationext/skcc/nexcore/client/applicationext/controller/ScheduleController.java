package skcc.nexcore.client.applicationext.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseController;
import skcc.nexcore.client.applicationext.entity.DefaultConstant;
import skcc.nexcore.client.applicationext.entity.ScheduleVO;
import skcc.nexcore.client.applicationext.schedule.ScheduleJobInitializer;
import skcc.nexcore.client.applicationext.service.ScheduleService;

public class ScheduleController extends BaseController {

	private ScheduleService scheduleService;
	private ScheduleJobInitializer scheduleJobInitializer;

	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void setScheduleJobInitializer(ScheduleJobInitializer scheduleJobInitializer) {
		this.scheduleJobInitializer = scheduleJobInitializer;
	}

	public ModelAndView listView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isSelectEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		List<ScheduleVO> entity_list = scheduleService.selectAll();
		ModelAndView mav = new ModelAndView("forward:/system/admin/scheduleList.jsp");
		mav.addObject("entity_list", entity_list);
		return mav;
	}

	public ModelAndView insertView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		ModelAndView mav = new ModelAndView("forward:/system/admin/scheduleInsert.jsp");
		return mav;
	}

	public ModelAndView updateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String jobId = request.getParameter("job_id");
		ScheduleVO entity_info = scheduleService.select(jobId);

		ModelAndView mav = new ModelAndView("forward:/system/admin/scheduleUpdate.jsp");
		mav.addObject("entity_info", entity_info);
		return mav;
	}

	public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		ScheduleVO entity = toScheduleVO(request);
		int row = scheduleService.insert(entity);
		if (row > 0) {
			if (scheduleJobInitializer != null) {
				scheduleJobInitializer.add(entity);
			}
		}
		ModelAndView mav = new ModelAndView("redirect:/system/admin/scheduleList.do");
		setStatusMessage(mav, true, "스케줄[" + entity.jobId + "] 등록을 " + (row > 0 ? "성공" : "실패") + "하였습니다.");
		return mav;
	}

	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isUpdateEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		ScheduleVO entity = toScheduleVO(request);
		int row = scheduleService.update(entity);
		if (row > 0) {
			if (scheduleJobInitializer != null) {
				scheduleJobInitializer.remove(entity.jobId);
				scheduleJobInitializer.add(entity);
			}
		}
		ModelAndView mav = new ModelAndView("redirect:/system/admin/scheduleList.do");
		setStatusMessage(mav, true, "스케줄[" + entity.jobId + "] 수정을 " + (row > 0 ? "성공" : "실패") + "하였습니다.");
		return mav;
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isDeleteEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String[] idList = request.getParameterValues("id_list");
		int row = scheduleService.delete(idList);
		if (row > 0 && idList != null) {
			for (String id : idList) {
				if (scheduleJobInitializer != null) {
					scheduleJobInitializer.remove(id);
				}
			}
		}
		ModelAndView mav = new ModelAndView("redirect:/system/admin/scheduleList.do");
		setStatusMessage(mav, true, "스케줄 삭제를 " + (row > 0 ? "성공" : "실패") + "하였습니다. 삭제건수:" + row);
		return mav;
	}

	public ModelAndView duplicateCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!isInsertEnabled()) {
			return DefaultConstant.ERROR_AUTH_PAGE;
		}

		String jobId = request.getParameter("job_id");
		ScheduleVO entity = scheduleService.select(jobId);
		boolean duplicated = false;
		if (entity != null) {
			duplicated = true;
		}
		ModelAndView mav = new ModelAndView("redirect:/system/common/duplicateCheck.do");
		mav.addObject("duplicated", "" + duplicated);
		return mav;
	}

	private ScheduleVO toScheduleVO(HttpServletRequest request) {
		ScheduleVO entity = new ScheduleVO();
		entity.jobId = request.getParameter("job_id");
		entity.jobName = request.getParameter("job_name");
		entity.jobDesc = request.getParameter("job_desc");
		entity.jobUrl = request.getParameter("job_url");
		entity.jobCronExpression = request.getParameter("job_cron_expression");
		entity.jobParams = request.getParameter("job_params");
		entity.jobEnabled = request.getParameter("job_enabled");
		return entity;
	}

}
