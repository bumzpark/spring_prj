package skcc.nexcore.client.application.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import skcc.nexcore.client.application.util.AlertMessages;
import skcc.nexcore.client.application.web.servlet.mvc.CustomMultiActionController;

public class BaseController extends CustomMultiActionController {

	protected final Log logger = LogFactory.getLog(getClass());

	protected void setStatusMessage(ModelAndView mav, boolean alert, String message) {
		setStatusMessage(mav, alert, message, null);
	}

	protected void setStatusMessage(ModelAndView mav, boolean alert, String message, String message_detail) {
		mav.addObject("status_alert", alert ? "true" : "false");
		//		mav.addObject("status_message", CharConversionUtils.encodeUniCode(message));
		//		mav.addObject("status_message_detail", CharConversionUtils.encodeUniCode(message_detail));
		//		mav.addObject("status_message", message);
		//		mav.addObject("status_message_detail", message_detail);

		if (message != null) {
			mav.addObject("status_message_id", AlertMessages.put(message));
		}
		if (message_detail != null) {
			mav.addObject("status_message_detail_id", AlertMessages.put(message_detail));
		}

		if (message != null || message_detail != null) {

			actionHistory(alert ? BaseActionHistory.FAIL : BaseActionHistory.SUCCESS, (message == null ? "" : message + "\n") + (message_detail == null ? "" : message_detail));
		}

	}

}
