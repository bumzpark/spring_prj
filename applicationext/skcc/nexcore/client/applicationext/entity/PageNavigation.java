package skcc.nexcore.client.applicationext.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.base.BaseVO;

public class PageNavigation extends BaseVO {

	private static final long serialVersionUID = 4595450619459356391L;

	public boolean isEmpty;
	public int rowPerPage;
	public int currentPage;
	public int totalPage = -1;
	public int limitStart;
	public int limitEnd;
	public int indexStart;
	public int indexEnd;
	public boolean isRequestLastPage;
	public boolean isRequestFirstPage;

	public boolean hasNextPage;
	public boolean hasPrevPage;
	
	private int totalCount;

	public static PageNavigation get(HttpServletRequest request) {
		PageNavigation pn = (PageNavigation) request.getAttribute("page_navigation");
		if (pn == null) {
			pn = new PageNavigation();
			pn.isEmpty = true;
		}
		return pn;
	}

	public static Map getMap(HttpServletRequest httpRequest) {
		Map map = new HashMap();
		map.put("row_per_page", httpRequest.getParameter("row_per_page"));
		map.put("current_page", httpRequest.getParameter("current_page"));
		map.put("total_page", httpRequest.getParameter("total_page"));
		return map;
	}

	public static PageNavigation request(HttpServletRequest httpRequest) {
		String rowPerPageStr = httpRequest.getParameter("row_per_page");
		String currentPageStr = httpRequest.getParameter("current_page");
		String totalPageStr = httpRequest.getParameter("total_page");

		PageNavigation request = new PageNavigation();

		try {
			request.rowPerPage = Integer.parseInt(rowPerPageStr);
		} catch (Exception e) {
		}
		if (request.rowPerPage < 1) {
			request.rowPerPage = 20;
		}

		try {
			request.totalPage = Integer.parseInt(totalPageStr);
		} catch (Exception e) {
		}

		if ("last".equalsIgnoreCase(currentPageStr)) {
			request.isRequestLastPage = true;
		} else {
			try {
				request.currentPage = Integer.parseInt(currentPageStr);
			} catch (Exception e) {
			}
			if (request.currentPage < 1) {
				request.currentPage = 1;
			}
		}

		if (request.totalPage > 0 && request.currentPage > request.totalPage) {
			request.currentPage = request.totalPage;
		}

		if (request.currentPage <= 1) {
			request.isRequestFirstPage = true;
		}

		request.limitStart = ((request.currentPage - 1) * request.rowPerPage);
		request.limitEnd = request.rowPerPage + 1;

		request.indexStart = request.limitStart + 1;
		request.indexEnd = request.indexStart + request.rowPerPage;
		return request;
	}

	public static void response(ModelAndView mav, PageNavigation navigation, List result) {
		if (result != null && result.size() > navigation.rowPerPage) {
			navigation.hasNextPage = true;
			result.remove(result.size() - 1);
		}
		if (navigation.currentPage > 1) {
			navigation.hasPrevPage = true;
		}

		mav.addObject("page_navigation", navigation);
	}

	public void setTotalCount(int totalCount) {
		
		this.totalCount = totalCount;
		
		int page = totalCount / rowPerPage;
		page += (totalCount % rowPerPage) > 0 ? 1 : 0;

		if (isRequestLastPage) {
			currentPage = page;
		}
		totalPage = page;

		if (totalPage > 0 && currentPage > totalPage) {
			currentPage = totalPage;
		}

		if (currentPage <= 1) {
			isRequestFirstPage = true;
		}

		limitStart = ((currentPage - 1) * rowPerPage);
		limitEnd = rowPerPage + 1;

		indexStart = limitStart + 1;
		indexEnd = indexStart + rowPerPage;
	}
	
	public int getTotalCount() {
		
		return this.totalCount;
	}

}
