package skcc.nexcore.client.application.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import skcc.nexcore.client.application.web.HttpParameterData;
import skcc.nexcore.client.application.web.SpringFactoryContext;

public class SpringFactoryServlet extends HttpServlet {

	public static final String HANDLER_EXECUTION_CHAIN_ATTRIBUTE = SpringFactoryServlet.class.getName() + ".HANDLER";

	private static final long serialVersionUID = -3900879671122205750L;

	private WebApplicationContext applicationContext;

	public void init() throws ServletException {
		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDispatch(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDispatch(request, response);
	}

	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		SpringFactoryContext ctx = null;
		try {
			InputStream is = request.getInputStream();
			ois = new ObjectInputStream(is);
			ctx = (SpringFactoryContext) ois.readObject();

			Object bean = applicationContext.getBean(ctx.bean);
			Method method = bean.getClass().getMethod(ctx.method, new Class[] { HttpServletRequest.class, HttpServletResponse.class, HttpParameterData.class });
			if (method != null) {
				method.invoke(bean, new Object[] { request, response, ctx.requestData });
			}

			oos = new ObjectOutputStream(response.getOutputStream());
			oos.writeObject(ctx);
		} catch (Exception e) {
			if (ctx == null) {
				ctx = new SpringFactoryContext();
				ctx.fail = true;
				ctx.message = e.getMessage();
				try {
					if (oos == null) {
						oos = new ObjectOutputStream(response.getOutputStream());
					}
					oos.writeObject(ctx);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.flush();
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
