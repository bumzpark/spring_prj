package skcc.nexcore.client.application.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import skcc.nexcore.client.application.base.BaseActionHistory;
import skcc.nexcore.client.application.base.BaseAuthority;
import skcc.nexcore.client.application.base.BaseAuthorityVO;
import skcc.nexcore.client.application.util.AuthorityHelper;
import skcc.nexcore.client.application.util.SessionHelper;
import skcc.nexcore.client.application.web.HttpParameterData;
import skcc.nexcore.client.application.web.bind.CustomServletRequestDataBinder;
import skcc.nexcore.client.foundation.mail.MailException;
import skcc.nexcore.client.foundation.mail.MailMessage;
import skcc.nexcore.client.foundation.mail.Mailer;

public class CustomMultiActionController extends MultiActionController implements BeanNameAware {

	private ThreadLocal<BaseAuthorityVO> authority_info_local = new ThreadLocal<BaseAuthorityVO>();
	private String beanName;
	private BaseAuthority baseAuthority;
	private BaseActionHistory baseActionHistory;
	private Mailer mailer;

	private String authorityFunctionId;

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setBaseAuthority(BaseAuthority baseAuthority) {
		this.baseAuthority = baseAuthority;
	}

	public void setBaseActionHistory(BaseActionHistory baseActionHistory) {
		this.baseActionHistory = baseActionHistory;
	}

	public void setMailer(Mailer mailer) {
		this.mailer = mailer;
	}

	public void setAuthorityFunctionId(String authorityFunctionId) {
		this.authorityFunctionId = authorityFunctionId;
	}

	/**
	 * Determine a handler method and invoke it.
	 * 
	 * @see MethodNameResolver#getHandlerMethodName
	 * @see #invokeNamedMethod
	 * @see #handleNoSuchRequestHandlingMethod
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			initAuthority(request);
			ModelAndView mav = super.handleRequestInternal(request, response);
			if (mav != null) {
				BaseAuthorityVO authority_info = authority_info_local.get();
				if (authority_info != null) {
					mav.addObject("authority_info", authority_info);
				}
			}
			return mav;
		} finally {
			authority_info_local.remove();
		}
	}

	/**
	 * Create a new binder instance for the given command and request.
	 * <p>
	 * Called by <code>bind</code>. Can be overridden to plug in custom
	 * ServletRequestDataBinder subclasses.
	 * <p>
	 * The default implementation creates a standard ServletRequestDataBinder,
	 * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
	 * will not be invoked if you override this method!
	 * 
	 * @param request
	 *            current HTTP request
	 * @param command
	 *            the command to bind onto
	 * @return the new binder instance
	 * @throws Exception
	 *             in case of invalid state or arguments
	 * @see #bind
	 * @see #initBinder
	 */
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
		ServletRequestDataBinder binder = null;
		if (command instanceof HttpParameterData) {
			HttpParameterData data = (HttpParameterData) command;
			binder = new CustomServletRequestDataBinder(command, getCommandName(command));
		} else {
			binder = new ServletRequestDataBinder(command, getCommandName(command));
		}
		initBinder(request, binder);
		return binder;
	}

	private void initAuthority(HttpServletRequest request) {
		String authoritySessionId = SessionHelper.getSessionId(request);
		String authorityUserId = SessionHelper.getString(request, SessionHelper.USER_ID);
		String authorityGroupId = SessionHelper.getString(request, SessionHelper.GROUP_ID);
		BaseAuthorityVO entity = new BaseAuthorityVO();
		entity.userId = authorityUserId;
		entity.sessionId = authoritySessionId;
		entity.groupId = authorityGroupId;
		entity.functionId = getAuthorityFunctionId();
		if (baseAuthority != null) {
			baseAuthority.check(entity);
		}
		authority_info_local.set(entity);
	}

	private String getAuthorityFunctionId() {
		return authorityFunctionId;
	}

	protected boolean isSelectEnabled() {
		return AuthorityHelper.isSelect(getBaseAuthority());
	}

	protected boolean isUpdateEnabled() {
		return AuthorityHelper.isUpdate(getBaseAuthority());
	}

	protected boolean isInsertEnabled() {
		return AuthorityHelper.isInsert(getBaseAuthority());
	}

	protected boolean isDeleteEnabled() {
		return AuthorityHelper.isDelete(getBaseAuthority());
	}

	protected boolean isServerEnabled() {
		return AuthorityHelper.isServer(getBaseAuthority());
	}

	protected void sendMail(MailMessage mm) throws MailException {
		mailer.sendMail(mm);
	}

	protected void actionHistory(String status, String desc) {
		if (baseActionHistory != null) {
			baseActionHistory.history(getBaseAuthority(), status, desc);
		}
	}

	private BaseAuthorityVO getBaseAuthority() {
		return authority_info_local.get();
	}

}